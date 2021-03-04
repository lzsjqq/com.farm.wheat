package com.farm.wheat.share.service.service.chan.impl;

import com.alibaba.fastjson.JSONObject;
import com.farm.wheat.share.service.dto.SharePriceDto;
import com.farm.wheat.share.service.mapper.simple.SharePriceMapper;
import com.farm.wheat.share.service.service.chan.IChanLunService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: wheat
 * @description:
 * @author: xyc
 * @create: 2020-03-13 21:59
 */
@Service
public class ChanLunServiceImpl implements IChanLunService {

    @Resource
    private SharePriceMapper sharePriceMapper;

    @Override
    public void build(String shareCode) {

        List<SharePriceDto> sharePrices = sharePriceMapper.selectSharePrices(shareCode);
        System.out.println(JSONObject.toJSONString(sharePrices));
    }
}