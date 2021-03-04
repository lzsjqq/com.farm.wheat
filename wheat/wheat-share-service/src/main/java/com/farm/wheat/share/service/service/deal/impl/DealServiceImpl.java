package com.farm.wheat.share.service.service.deal.impl;

import com.farm.common.utils.ConvertUtil;
import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.service.dto.*;
import com.farm.wheat.share.service.mapper.simple.DealDetailInfoMapper;
import com.farm.wheat.share.service.mapper.simple.DealInfoMapper;
import com.farm.wheat.share.service.mapper.simple.EventMapper;
import com.farm.wheat.share.service.po.DealDetailInfoPO;
import com.farm.wheat.share.service.po.DealInfoPO;
import com.farm.wheat.share.service.po.EventPO;
import com.farm.wheat.share.service.service.deal.DealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录相关
 */
@Service
public class DealServiceImpl implements DealService {
    @Resource
    private DealInfoMapper dealInfoMapper;
    @Resource
    private DealDetailInfoMapper dealDetailInfoMapper;
    @Resource
    private EventMapper eventMapper;

    @Override
    public PageInfo<DealInfoPO> dealInfo(DealInfoDTO record) {
        PageHelper.startPage(record.getPageNum(), record.getPageSize());
        Page<DealInfoPO> shareInfos = dealInfoMapper.selectBySelective(record);
        return new PageInfo(shareInfos);
    }

    @Override
    public PageInfo<DealDetailInfoPO> dealDetailInfo(DealDetailInfoDTO record) {
        PageHelper.startPage(record.getPageNum(), record.getPageSize());
        Page<DealDetailInfoPO> shareInfos = dealDetailInfoMapper.selectBySelective(record);
        return new PageInfo(shareInfos);
    }

    @Override
    public void completeDeal(String shareCode) throws Exception {
        DealInfoPO dealInfoPO = dealInfoMapper.selectByShareCode(shareCode, DealInfoStatusEnum.WC.getValue());
        if (0 < dealInfoPO.getVolume()) {
            throw new Exception("尚未清空股票，不能完成的哟!");
        }
        dealInfoMapper.completeDeal(shareCode);
    }

    @Override
    public void insertEvent(EventPO convert) {
        Date eventDate = convert.getEventDate();
        if (null == eventDate) {
            convert.setEventDate(new Date());
        }
        eventMapper.insert(convert);
    }

    @Override
    public void updateDealInfo(DealInfoDTO dealInfoDTO) throws Exception {
        DealInfoPO dealInfoPO = dealInfoMapper.selectByPrimaryKey(dealInfoDTO.getIdDealInfo());
        String analyse = dealInfoPO.getAnalyse();
        String analyseOne = dealInfoDTO.getAnalyseOne();
        dealInfoDTO.setAnalyse(getAnalyse(analyse, analyseOne));
        dealInfoMapper.updateByPrimaryKeySelective(ConvertUtil.convert(dealInfoDTO, DealInfoPO.class));
    }

    @Override
    public PageInfo<EventPO> listEvents(EntityDto record) {
        PageHelper.startPage(record.getPageNum(), record.getPageSize());
        return new PageInfo<>(eventMapper.selectAll());
    }

    private String getAnalyse(String analyse, String analyseOne) throws Exception {
        StringBuilder rel = new StringBuilder();
        if (NullCheckUtils.isBlank(analyseOne)) {
            return analyse;
        }
        String data = DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD);
        if (NullCheckUtils.isBlank(analyse)) {
            rel.append("<b>" + data + "：</b>");
            rel.append(analyseOne);
        } else {
            rel.append(analyse);
            rel.append("</br>");
            rel.append("<b>" + data + "：</b>");
            rel.append(analyseOne);
        }
        return rel.toString().replace("\n", "</br>");
    }

    @Transactional
    @Override
    public int insertDetail(DealDetailInfoDTO dealDetailInfoDTO) throws Exception {
        // 计算交易时间
        String tradingDate = dealDetailInfoDTO.getTradingDate();
        if (NullCheckUtils.isBlank(tradingDate)) {
            tradingDate = DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD);
        }
        BigDecimal dealPrice = dealDetailInfoDTO.getDealPrice();
        Integer volumeNow = dealDetailInfoDTO.getVolume();
        // 当次的市值
        BigDecimal multiplyNowThisTime = getMultiply(dealPrice, volumeNow);
        // 当次的手续费
        BigDecimal changeMoney = getChangeMoney(multiplyNowThisTime);
        // 当次的印花税
        BigDecimal stampDuty = getStampDuty(multiplyNowThisTime, dealDetailInfoDTO.getTarget());
        // 查询以往投资记录总记录
        DealInfoPO dealInfoPO = dealInfoMapper.selectByShareCode(dealDetailInfoDTO.getShareCode(), DealInfoStatusEnum.CC.getValue());
        Integer idDealInfo;
        // 当次收益
        BigDecimal spendNow = changeMoney.add(stampDuty).multiply(new BigDecimal(-1));
        DealTargetEnum target = dealDetailInfoDTO.getTarget();
        if (dealInfoPO == null) {
            DealInfoPO record = buildDealInfoPO(dealDetailInfoDTO, changeMoney, spendNow);
            dealInfoMapper.insertSelective(record);
            idDealInfo = record.getIdDealInfo();
        } else {
            idDealInfo = dealInfoPO.getIdDealInfo();
            // 设置总手续费
            dealInfoPO.setChangeMoney(changeMoney.add(dealInfoPO.getChangeMoney() == null ? BigDecimal.ZERO : dealInfoPO.getChangeMoney()));
            // 设置总印花税
            dealInfoPO.setStampDuty(stampDuty.add(dealInfoPO.getStampDuty() == null ? BigDecimal.ZERO : dealInfoPO.getStampDuty()));
            int totalVolume = getVolume(volumeNow, dealInfoPO.getVolume(), target);
            // 设置当前总手数
            dealInfoPO.setVolume(totalVolume);
            // 获取当前总市值
            BigDecimal multiply = getMultiply(dealInfoPO.getFirstCost(), dealInfoPO.getVolume());
            // 获取当前每股成本价
            BigDecimal costNow = getCostNow(multiplyNowThisTime, totalVolume, multiply);
            BigDecimal profitNow = getProfitNow(multiplyNowThisTime, dealInfoPO.getFirstCost(), volumeNow, target);
            BigDecimal profit = getProfit(dealInfoPO.getProfit(), profitNow, spendNow, target);
            dealInfoPO.setFirstCost(costNow);
            dealInfoPO.setProfit(profit);
        }
        dealDetailInfoDTO.setTradingDate(tradingDate);
        dealDetailInfoDTO.setPlan(getPlan(dealDetailInfoDTO.getPlan()));
        dealDetailInfoDTO.setStampDuty(stampDuty);
        dealDetailInfoDTO.setChangeMoney(changeMoney);
        dealDetailInfoDTO.setIdDealInfo(idDealInfo);
        return dealDetailInfoMapper.insert(dealDetailInfoDTO);
    }

    /**
     * @param multiplyNowThisTime 当次的成交金额
     * @param firstCostHis        历史成本
     * @param volumeNow           当次数量
     * @return BigDecimal
     */
    private BigDecimal getProfitNow(BigDecimal multiplyNowThisTime, BigDecimal firstCostHis, Integer volumeNow, DealTargetEnum target) {
        if (DealTargetEnum.MR == target) {
            return BigDecimal.ZERO;
        } else {
            return multiplyNowThisTime.subtract(firstCostHis.multiply(new BigDecimal(volumeNow)));
        }
    }

    /**
     * 获取收益
     * <p>
     * 买入：历史收益+当前成本
     * 卖出：历史收益+当前收益+当前成本
     *
     * @param profitHistory 历史收益
     * @param profitNow     当前收益
     * @param spendNow      当前成本
     * @param target        买卖方向
     * @return
     */
    private BigDecimal getProfit(BigDecimal profitHistory, BigDecimal profitNow, BigDecimal spendNow, DealTargetEnum target) {
        if (DealTargetEnum.MR == target) {
            return profitHistory.add(spendNow);
        } else {
            return profitHistory.add(profitNow).add(spendNow);
        }
    }

    /**
     * 计算当前成本价
     *
     * @param multiplyNowThisTime
     * @param totalVolume
     * @param multiplyHistory
     * @return
     */
    private BigDecimal getCostNow(BigDecimal multiplyNowThisTime, int totalVolume, BigDecimal multiplyHistory) {
        return multiplyNowThisTime.add(multiplyHistory).divide(new BigDecimal(totalVolume)).setScale(3, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 获取成交后的股数
     *
     * @param volumeNow     当次成交的股数
     * @param volumeHistory 历史股数
     * @return
     */
    private int getVolume(Integer volumeNow, Integer volumeHistory, DealTargetEnum target) throws Exception {
        int now = DealTargetEnum.MR == target ? volumeHistory + volumeNow : volumeHistory - volumeNow;
        if (now < 0) {
            throw new Exception("小于零了哦！");
        }
        return now;
    }

    /**
     * 获取交易时市值
     *
     * @param dealPrice 成交价
     * @param volumeNow 成交量
     * @return BigDecimal
     */
    private BigDecimal getMultiply(BigDecimal dealPrice, Integer volumeNow) {
        return dealPrice.multiply(new BigDecimal(volumeNow));
    }

    private DealInfoPO buildDealInfoPO(DealDetailInfoDTO dealDetailInfoDTO, BigDecimal changeMoney, BigDecimal profitNow) {
        DealInfoPO record = new DealInfoPO();
        Integer volume = dealDetailInfoDTO.getVolume();
        record.setShareCode(dealDetailInfoDTO.getShareCode());
        record.setShareName(dealDetailInfoDTO.getShareName());
        record.setVolume(volume);
        record.setFirstCost(getMultiply(dealDetailInfoDTO.getDealPrice(), volume).add(dealDetailInfoDTO.getChangeMoney()).add(dealDetailInfoDTO.getStampDuty()).divide(new BigDecimal(volume)).setScale(3, BigDecimal.ROUND_HALF_UP));
        record.setChangeMoney(changeMoney);
        // 印花税+手续费
        record.setProfit(profitNow);
        return record;
    }

    /**
     * 计算手续费
     *
     * @param multiplyNow multiplyNow
     * @return BigDecimal
     */
    private BigDecimal getChangeMoney(BigDecimal multiplyNow) {
        BigDecimal stampDuty;
        BigDecimal changeMoney;
        if (multiplyNow.doubleValue() <= 10000) {
            changeMoney = new BigDecimal(5);
        } else {
            changeMoney = multiplyNow.multiply(new BigDecimal(2.5)).divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        return changeMoney;
    }

    /**
     * 计算印花税
     *
     * @param multiplyNow multiplyNow
     * @return BigDecimal
     */
    private BigDecimal getStampDuty(BigDecimal multiplyNow, DealTargetEnum target) {
        if (DealTargetEnum.MR == target) {
            return BigDecimal.ZERO;
        }
        return multiplyNow.multiply(new BigDecimal(1)).divide(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private String getPlan(String plan) {
        StringBuilder sb = new StringBuilder();
        String[] split = plan.split(";");
        for (int i = 0; i < split.length; i++) {
            if (i != split.length - 1) {
                sb.append(split[i] + "\r\n");
                continue;
            }
            sb.append(split[i]);
        }
        return sb.toString();
    }
}
