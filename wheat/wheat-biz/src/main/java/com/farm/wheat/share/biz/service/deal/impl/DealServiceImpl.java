package com.farm.wheat.share.biz.service.deal.impl;

import com.farm.common.utils.ConvertUtil;
import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.dto.*;
import com.farm.wheat.share.biz.mapper.simple.DealDetailInfoMapper;
import com.farm.wheat.share.biz.mapper.simple.DealInfoMapper;
import com.farm.wheat.share.biz.mapper.simple.EventMapper;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.farm.wheat.share.biz.po.EventPO;
import com.farm.wheat.share.biz.service.deal.DealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        return rel.toString();
    }

    @Transactional
    @Override
    public int insertDetail(DealDetailInfoDTO dealDetailInfoDTO) throws Exception {
        String tradingDate = dealDetailInfoDTO.getTradingDate();
        if (NullCheckUtils.isBlank(tradingDate)) {
            tradingDate = DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD);
        }
        BigDecimal stopLossPrice = dealDetailInfoDTO.getStopLossPrice();
        String target = dealDetailInfoDTO.getTarget();
        if (DealTargetEnum.MR.equals(target) && null == stopLossPrice) {
            throw new Exception("止损价格不能为空哦！");
        }
        dealDetailInfoDTO.setTradingDate(tradingDate);
        String plan = dealDetailInfoDTO.getPlan();
        plan = getPlan(plan);
        dealDetailInfoDTO.setPlan(plan);
        String shareCode = dealDetailInfoDTO.getShareCode();
        // 查询未完成的
        DealInfoPO dealInfoPO = dealInfoMapper.selectByShareCode(shareCode, DealInfoStatusEnum.CC.getValue());
        Integer idDealInfo;
        Integer volumeNow = dealDetailInfoDTO.getVolume();
        BigDecimal dealPrice = dealDetailInfoDTO.getDealPrice();
        BigDecimal multiplyNow = dealPrice.multiply(new BigDecimal(volumeNow));
        BigDecimal changeMoney = getChangeMoney(multiplyNow);
        BigDecimal stampDuty = getStampDuty(multiplyNow);
        if (dealInfoPO == null) {
            dealDetailInfoDTO.setChangeMoney(changeMoney);
            DealInfoPO record = new DealInfoPO();
            record.setShareCode(shareCode);
            record.setShareName(dealDetailInfoDTO.getShareName());
            record.setVolume(dealDetailInfoDTO.getVolume());
            record.setFirstCost(dealDetailInfoDTO.getDealPrice());
            record.setChangeMoney(changeMoney);
            if (!DealTargetEnum.MR.equals(target)) {
                record.setStampDuty(stampDuty);
            }
            dealInfoMapper.insertSelective(record);
            idDealInfo = record.getIdDealInfo();
        } else {
            idDealInfo = dealInfoPO.getIdDealInfo();
            dealInfoPO.setChangeMoney(changeMoney.add(dealInfoPO.getChangeMoney() == null ? BigDecimal.ZERO : dealInfoPO.getChangeMoney()));
            if (DealTargetEnum.MR.equals(target)) {
                Integer volume = dealInfoPO.getVolume();
                BigDecimal firstCost = dealInfoPO.getFirstCost();
                BigDecimal multiply = firstCost.multiply(new BigDecimal(volume));
                Integer now = volume + volumeNow;
                BigDecimal costNow = multiplyNow.add(multiply).divide(new BigDecimal(now)).setScale(2, BigDecimal.ROUND_HALF_UP);
                dealInfoPO.setFirstCost(costNow);
                dealInfoPO.setVolume(now);
            } else {
                Integer volume = dealInfoPO.getVolume();
                BigDecimal firstCost = dealInfoPO.getFirstCost();
                BigDecimal multiply = firstCost.multiply(new BigDecimal(volume));
                Integer now = volume - volumeNow;

                if (now < 0) {
                    throw new Exception("小于零了哦！");
                }
                dealDetailInfoDTO.setStampDuty(stampDuty);
                dealDetailInfoDTO.setChangeMoney(changeMoney);
//                BigDecimal costNow = multiply.subtract(multiplyNow).divide(new BigDecimal(now)).setScale(3, BigDecimal.ROUND_HALF_UP);
//                dealInfoPO.setFirstCost(costNow);
                dealInfoPO.setVolume(now);
                dealInfoPO.setStampDuty(stampDuty.add(dealInfoPO.getStampDuty() == null ? BigDecimal.ZERO : dealInfoPO.getStampDuty()));
                dealDetailInfoDTO.setChangeMoney(changeMoney);
            }
            dealInfoMapper.updateByPrimaryKey(dealInfoPO);
        }
        dealDetailInfoDTO.setIdDealInfo(idDealInfo);
        return dealDetailInfoMapper.insert(dealDetailInfoDTO);
    }

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

    private BigDecimal getStampDuty(BigDecimal multiplyNow) {
        BigDecimal stampDuty = multiplyNow.multiply(new BigDecimal(1)).divide(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP);
        return stampDuty;
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
