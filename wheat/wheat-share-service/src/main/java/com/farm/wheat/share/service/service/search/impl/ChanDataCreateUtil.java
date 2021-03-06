package com.farm.wheat.share.service.service.search.impl;

import com.alibaba.fastjson.JSONObject;
import com.farm.common.utils.NullCheckUtils;
import com.farm.wheat.share.service.dto.SharePriceDto;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 数据创造
 * @author: xyc
 * @create: 2021-03-06 19:22
 */
public class ChanDataCreateUtil {
    /**
     * 都是1
     */
    public static final String ITEM = "{\"amplitude\":2.57,\"priceChange\":-0.01,\"priceChangeRatio\":-0.26,\"quarter\":1,\"todayEndPrice\":1,\"todayMaxPrice\":1,\"todayMinPrice\":0,\"todayOpenPrice\":0,\"tradingMoney\":3805,\"tradingVolume\":98226}";

    public static SharePriceDto crtSharePriceDto() {
        return JSONObject.parseObject(ITEM, SharePriceDto.class);
    }

    /**
     * 底分型
     *
     * @return List<SharePriceDto
     */
    public static List<SharePriceDto> crtBottom(List<SharePriceDto> org) {
        org = init(org);
        SharePriceDto downSharePriceDto = getDownSharePriceDto(getLastOne(org));
        if(NullCheckUtils.isNotBlank(downSharePriceDto)){
            org.add(downSharePriceDto);
        }
        org.add(getUpSharePriceDto(getLastOne(org)));
        return org;
    }

    /**
     * 顶分型
     *
     * @return List<SharePriceDto
     */
    public static List<SharePriceDto> crtTop(List<SharePriceDto> org) {
        org = init(org);
        org.add(getUpSharePriceDto(getLastOne(org)));
        org.add(getDownSharePriceDto(getLastOne(org)));
        return org;
    }

    public static SharePriceDto getLastOne(List<SharePriceDto> org) {
        return org.get(org.size() - 1);
    }

    public static List<SharePriceDto> init(List<SharePriceDto> org) {
        if (NullCheckUtils.isBlank(org)) {
            org = new ArrayList<>();
            org.add(crtSharePriceDto());
            return org;
        }
        return org;
    }

    /**
     * 左包含,左边大
     *
     * @return List<SharePriceDto
     */
    public static List<SharePriceDto> crtLeftContain(List<SharePriceDto> org) {
        org = init(org);
        org.add(subtractOne(getLastOne(org)));
        return org;
    }

    /**
     * 右包含,右边大
     *
     * @return List<SharePriceDto
     */
    public static List<SharePriceDto> crtRightContain(List<SharePriceDto> org) {
        org = init(org);
        org.add(addOne(getLastOne(org)));
        return org;
    }

    /**
     * 向上
     *
     * @return List<SharePriceDto
     */
    public static List<SharePriceDto> crtUp(List<SharePriceDto> org, int time) {
        org = init(org);
        if (time == 0) {
            time = crtRandom();
        }
        for (int i = 0; i < time; i++) {
            SharePriceDto second = getUpSharePriceDto(getLastOne(org));
            org.add(second);
        }
        return org;
    }

    public static SharePriceDto getUpSharePriceDto(SharePriceDto first) {
        SharePriceDto second = crtSharePriceDto();
        BigDecimal minPrice = first.getTodayMinPrice().add(new BigDecimal(1));
        BigDecimal maxPrice = first.getTodayMaxPrice().add(new BigDecimal(1));
        BigDecimal subtract = maxPrice.subtract(minPrice);
        if (subtract.compareTo(BigDecimal.ONE) > 0) {
            minPrice = maxPrice.subtract(BigDecimal.ONE);
        }
        second.setTodayOpenPrice(minPrice);
        second.setTodayMinPrice(minPrice);
        second.setTodayMaxPrice(maxPrice);
        second.setTodayEndPrice(maxPrice);
        return second;
    }

    /**
     * 向下
     *
     * @return List<SharePriceDto
     */
    public static List<SharePriceDto> crtDown(List<SharePriceDto> org, int time) {
        org = init(org);
        if (time == 0) {
            time = crtRandom();
        }
        for (int i = 0; i < time; i++) {
            SharePriceDto second = getDownSharePriceDto(getLastOne(org));
            if (second == null) break;
            org.add(second);
        }
        return org;
    }

    public static SharePriceDto getDownSharePriceDto(SharePriceDto first) {
        SharePriceDto second = crtSharePriceDto();
        BigDecimal minPrice = first.getTodayMinPrice().subtract(new BigDecimal(1));
        BigDecimal maxPrice = first.getTodayMaxPrice().subtract(new BigDecimal(1));
        if (minPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        BigDecimal subtract = maxPrice.subtract(minPrice);
        if (subtract.compareTo(BigDecimal.ONE) > 0) {
            maxPrice = minPrice.add(BigDecimal.ONE);
        }
        second.setTodayOpenPrice(minPrice);
        second.setTodayMinPrice(minPrice);
        second.setTodayMaxPrice(maxPrice);
        second.setTodayEndPrice(maxPrice);
        return second;
    }


    public static int crtRandom() {
        SecureRandom random = new SecureRandom();
        return random.nextInt() % 10;
    }

    /**
     * 加一
     *
     * @param sharePriceDto sharePriceDto
     */
    public static SharePriceDto addOne(SharePriceDto sharePriceDto) {
        SharePriceDto second = crtSharePriceDto();

        second.setTodayOpenPrice(sharePriceDto.getTodayOpenPrice().add(new BigDecimal(0.5)));
        second.setTodayMinPrice(sharePriceDto.getTodayMinPrice().add(new BigDecimal(0.5)));
        second.setTodayEndPrice(sharePriceDto.getTodayEndPrice().subtract(new BigDecimal(0.5)));
        second.setTodayMaxPrice(sharePriceDto.getTodayMaxPrice().subtract(new BigDecimal(0.5)));
        return second;
    }

    /**
     * 减一倍
     *
     * @param sharePriceDto sharePriceDto
     */
    public static SharePriceDto subtractOne(SharePriceDto sharePriceDto) {
        SharePriceDto second = crtSharePriceDto();
        second.setTodayEndPrice(sharePriceDto.getTodayEndPrice().add(new BigDecimal(0.5)));
        second.setTodayMaxPrice(sharePriceDto.getTodayMaxPrice().add(new BigDecimal(0.5)));
        second.setTodayOpenPrice(sharePriceDto.getTodayOpenPrice().subtract(new BigDecimal(0.5)));
        second.setTodayMinPrice(sharePriceDto.getTodayMinPrice().subtract(new BigDecimal(0.5)));
        return second;
    }
}