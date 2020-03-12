package com.farm.wheat.share.biz.service.search.impl;

import com.farm.common.utils.DateUtils;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.biz.dto.DrawLineDTO;
import com.farm.wheat.share.biz.dto.SharePriceDto;
import com.farm.wheat.share.biz.mapper.simple.SharePriceMapper;
import com.farm.wheat.share.biz.service.search.IDrawLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DrawLineService implements IDrawLineService {

    @Resource
    private SharePriceMapper sharePriceMapper;


    @Override
    public List<Object[]> sharePrices(DrawLineDTO drawLine) throws Exception {
        List<SharePriceDto> sharePrices = sharePriceMapper.selectSharePrices(drawLine.getShareCode());
        return convert(sharePrices);
    }

    /**
     * 数据意义：2013/1/24 ,开盘(open)，收盘(close)，最低(lowest)，最高(highest)
     *
     * @param sharePrices
     * @return
     */

    private List<Object[]> convert(List<SharePriceDto> sharePrices) throws Exception {
        List<Object[]> result = new ArrayList<>();
        if (NullCheckUtils.isNotBlank(sharePrices)) {
            Object[] arr;
            for (SharePriceDto sharePrice : sharePrices) {
                arr = new Object[5];
                arr[0] = DateUtils.dateToString(sharePrice.getTradingDate(), "yyyy/M/dd");
                arr[1] = sharePrice.getTodayOpenPrice().doubleValue();
                arr[2] = sharePrice.getTodayEndPrice().doubleValue();
                arr[3] = sharePrice.getTodayMinPrice().doubleValue();
                arr[4] = sharePrice.getTodayMaxPrice().doubleValue();
                result.add(arr);
            }
        }
        return result;
    }
}
