package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class SharePriceDto extends  EntityDto {
    private Integer idSharePrice;

    private Integer idShareInfo;

    private BigDecimal todayOpenPrice;

    private BigDecimal yesterdayEndPrice;

    private BigDecimal todayPrice;

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

}