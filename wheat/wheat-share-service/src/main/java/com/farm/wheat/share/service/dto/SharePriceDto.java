package com.farm.wheat.share.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SharePriceDto extends EntityDto {
    private Integer idSharePrice;

    private Integer idShareInfo;

    private Integer quarter;

    private Integer year;

    private BigDecimal todayOpenPrice;


    /**
     * 昨日收盘价
     */
    private BigDecimal yesterdayEndPrice;

    /**
     * 今日收盘价
     */
    private BigDecimal todayEndPrice;

    private BigDecimal todayMaxPrice;

    private BigDecimal todayMinPrice;

    private Integer tradingVolume;

    private Integer theOuter;

    private Integer theInner;

    private BigDecimal tradingMoney;

    private Date tradingDate;

    private BigDecimal priceChangeRatio;

    private BigDecimal priceChange;

    private BigDecimal turnoverRate;

    private BigDecimal amplitude;
    private BigDecimal peRatio;
    private BigDecimal pbRatio;
}
