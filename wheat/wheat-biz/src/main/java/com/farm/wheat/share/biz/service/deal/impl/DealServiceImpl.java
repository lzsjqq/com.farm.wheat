package com.farm.wheat.share.biz.service.deal.impl;

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

    @Transactional
    @Override
    public int insertDetail(DealDetailInfoDTO dealDetailInfoDTO) throws Exception {
        String tradingDate = dealDetailInfoDTO.getTradingDate();
        if (NullCheckUtils.isBlank(tradingDate)) {
            tradingDate = DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD);
        }
        dealDetailInfoDTO.setTradingDate(tradingDate);
        String plan = dealDetailInfoDTO.getPlan();
        StringBuilder sb = new StringBuilder();
        String[] split = plan.split(";");
        for (int i = 0; i < split.length; i++) {
            if (i != split.length - 1) {
                sb.append(split[i] + "\r\n");
                continue;
            }
            sb.append(split[i]);
        }
        dealDetailInfoDTO.setPlan(sb.toString());

        String shareCode = dealDetailInfoDTO.getShareCode();
        // 查询未完成的
        DealInfoPO dealInfoPO = dealInfoMapper.selectByShareCode(shareCode, DealInfoStatusEnum.CC.getValue());
        Integer idDealInfo;
        if (dealInfoPO == null) {
            DealInfoPO record = new DealInfoPO();
            record.setShareCode(shareCode);
            record.setShareName(dealDetailInfoDTO.getShareName());
            record.setVolume(dealDetailInfoDTO.getVolume());
            record.setFirstCost(dealDetailInfoDTO.getDealPrice());
            idDealInfo = record.getIdDealInfo();
            dealInfoMapper.insertSelective(record);
        } else {
            idDealInfo = dealInfoPO.getIdDealInfo();
            String target = dealDetailInfoDTO.getTarget();
            if (DealTargetEnum.MR.equals(target)) {
                Integer volume = dealInfoPO.getVolume();
                BigDecimal firstCost = dealInfoPO.getFirstCost();
                BigDecimal multiply = firstCost.multiply(new BigDecimal(volume));
                Integer volumeNow = dealDetailInfoDTO.getVolume();
                BigDecimal dealPrice = dealDetailInfoDTO.getDealPrice();
                BigDecimal multiplyNow = dealPrice.multiply(new BigDecimal(volumeNow));
                Integer now = volume + volumeNow;
                BigDecimal costNow = multiplyNow.add(multiply).divide(new BigDecimal(now)).setScale(3, BigDecimal.ROUND_HALF_UP);
                dealInfoPO.setFirstCost(costNow);
                dealInfoPO.setVolume(now);
                dealInfoMapper.updateByPrimaryKey(dealInfoPO);
            } else {
                Integer volume = dealInfoPO.getVolume();
                BigDecimal firstCost = dealInfoPO.getFirstCost();
                BigDecimal multiply = firstCost.multiply(new BigDecimal(volume));
                Integer volumeNow = dealDetailInfoDTO.getVolume();
                BigDecimal dealPrice = dealDetailInfoDTO.getDealPrice();
                BigDecimal multiplyNow = dealPrice.multiply(new BigDecimal(volumeNow));
                Integer now = volume - volumeNow;
                BigDecimal costNow = multiply.subtract(multiplyNow).divide(new BigDecimal(now)).setScale(3, BigDecimal.ROUND_HALF_UP);
                if (now < 0) {
                    throw new Exception("小于零了哦！");
                }
                dealInfoPO.setFirstCost(costNow);
                dealInfoPO.setVolume(now);
                dealInfoMapper.updateByPrimaryKey(dealInfoPO);
            }

        }

        dealDetailInfoDTO.setIdDealInfo(idDealInfo);
        return dealDetailInfoMapper.insert(dealDetailInfoDTO);
    }
}
