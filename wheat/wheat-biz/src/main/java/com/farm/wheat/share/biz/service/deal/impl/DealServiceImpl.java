package com.farm.wheat.share.biz.service.deal.impl;

import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.mapper.simple.DealDetailInfoMapper;
import com.farm.wheat.share.biz.mapper.simple.DealInfoMapper;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.farm.wheat.share.biz.service.deal.DealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.ws.soap.Addressing;
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

    @Transactional
    @Override
    public int insertDetail(DealDetailInfoPO dealDetailInfoPO) throws Exception {
        Date tradingDate = dealDetailInfoPO.getTradingDate();
        if (null == tradingDate) {
            dealDetailInfoPO.setTradingDate(DateUtils.dateToDate(new Date(), DateUtils.YYYY_MM_DD));
        }
        String reason = dealDetailInfoPO.getReason();
        StringBuilder sb=new StringBuilder();
        String[] split = reason.split(";");
        for (int i = 0; i < split.length; i++) {
            if(i!=split.length-1){
                sb.append(split[i]+"\n");
            }
            sb.append(split[i]);
        }
        dealDetailInfoPO.setReason(sb.toString());
        int insert = dealDetailInfoMapper.insert(dealDetailInfoPO);
        String shareCode = dealDetailInfoPO.getShareCode();



        DealInfoPO dealInfoPO = dealInfoMapper.selectByShareCode(shareCode);
        if (dealInfoPO == null) {
            DealInfoPO record = new DealInfoPO();
            record.setShareCode(shareCode);
            record.setShareName(dealDetailInfoPO.getShareName());
            record.setVolume(dealDetailInfoPO.getVolume());
            record.setFirstCost(dealDetailInfoPO.getDealPrice());
            dealInfoMapper.insertSelective(record);
        } else {
            String target = dealDetailInfoPO.getTarget();
            if ("1".equals(target)) {
                Integer volume = dealInfoPO.getVolume();
                BigDecimal firstCost = dealInfoPO.getFirstCost();
                BigDecimal multiply = firstCost.multiply(new BigDecimal(volume));
                Integer volumeNow = dealDetailInfoPO.getVolume();
                BigDecimal dealPrice = dealDetailInfoPO.getDealPrice();
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
                Integer volumeNow = dealDetailInfoPO.getVolume();
                BigDecimal dealPrice = dealDetailInfoPO.getDealPrice();
                BigDecimal multiplyNow = dealPrice.multiply(new BigDecimal(volumeNow));
                Integer now = volume - volumeNow;
                BigDecimal costNow = multiply.subtract(multiplyNow).divide(new BigDecimal(now)).setScale(3, BigDecimal.ROUND_HALF_UP);
                dealInfoPO.setFirstCost(costNow);
                dealInfoPO.setVolume(now);
                dealInfoMapper.updateByPrimaryKey(dealInfoPO);
            }

        }

        return insert;
    }
}
