package com.farm.wheat.share.service.service.data.impl;

import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.service.dto.ShareInfoDto;
import com.farm.wheat.share.service.dto.SharePriceDto;
import com.farm.wheat.share.service.vo.ShareTurnoverRateVO;
import com.farm.wheat.share.service.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.service.mapper.simple.SharePriceMapper;
import com.farm.wheat.share.service.mapper.simple.ShareTurnoverRateMapper;
import com.farm.wheat.share.service.service.data.RateCalculateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-13 21:36
 */
@Service
public class RateCalculateServiceImpl implements RateCalculateService {

    @Resource
    private SharePriceMapper sharePriceMapper;

    @Resource
    private ShareInfoMapper shareInfoMapper;

    @Resource
    private ShareTurnoverRateMapper shareTurnoverRateMapper;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    @Override
    @Transactional
    public void rateCalculate() {
        List<ShareInfoDto> shareInfos = shareInfoMapper.selectAll();
        if (NullCheckUtils.isBlank(shareInfos)) {
            return;
        }

        for (ShareInfoDto shareInfo : shareInfos) {
            fixedThreadPool.execute(() -> {
                Integer idShareInfo = shareInfo.getIdShareInfo();
                List<SharePriceDto> sharePrices = sharePriceMapper.selectByIdShareInfo(idShareInfo, 10);
                if (NullCheckUtils.isBlank(sharePrices) || sharePrices.size() < 10) {
                    return;
                }
                // 降序
                sharePrices.sort((t1, t2) -> {
                    long time1 = t1.getTradingDate().getTime();
                    long time2 = t2.getTradingDate().getTime();
                    if (time1 > time2) {
                        return -1;
                    } else if (time1 == time2) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                SharePriceDto sharePriceDto = sharePrices.get(0);
                ShareTurnoverRateVO shareTurnoverRateDTO = new ShareTurnoverRateVO();
                shareTurnoverRateDTO.setIdShareInfo(idShareInfo);
                BigDecimal turnoverRate = sharePriceDto.getTurnoverRate();
                shareTurnoverRateDTO.setNewRate(turnoverRate);
                BigDecimal rate3 = getRate(sharePrices, 3);
                shareTurnoverRateDTO.setThreeRate(rate3);
                shareTurnoverRateDTO.setThreeChangeRate(rate3.subtract(turnoverRate));
                BigDecimal rate5 = getRate(sharePrices, 5);
                shareTurnoverRateDTO.setFiveChangeRate(rate5.subtract(turnoverRate));
                shareTurnoverRateDTO.setFiveRate(rate5);
                BigDecimal rate10 = getRate(sharePrices, 10);
                shareTurnoverRateDTO.setTenChangeRate(rate10.subtract(turnoverRate));
                shareTurnoverRateDTO.setTenRate(rate10);
                shareTurnoverRateMapper.insertSelective(shareTurnoverRateDTO);
            });
        }
    }


    private BigDecimal getRate(List<SharePriceDto> shareInfos, int num) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < num; i++) {
            SharePriceDto sharePriceDto = shareInfos.get(i);
            total = total.add(sharePriceDto.getTurnoverRate());
        }
        return total.divide(new BigDecimal(num), 2, BigDecimal.ROUND_HALF_DOWN);
    }

}
