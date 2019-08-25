package com.farm.wheat.share.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-24 23:11
 */
@Data
public class SharePriceBaseDTO extends ShareBaseInfoDTO {

    /**
     * 今日开盘价
     */
    private BigDecimal todayOpenPrice;
    /**
     * 昨日收盘价
     */
    private BigDecimal yesterdayEndPrice;
    /**
     * 当前价格
     */
    private BigDecimal todayPrice;
    /**
     * 今日最高价
     */
    private BigDecimal todayMaxPrice;

    /**
     * 今日最低价
     */
    private BigDecimal todayMinPrice;
    /**
     * 成交量：手
     */
    private Integer tradingVolume;
    /**
     * 外盘：手
     */
    private Integer theOuter;
    /**
     * 内盘：手
     */
    private Integer theInner;
    /**
     * 成交金额：万
     */
    private BigDecimal tradingMoney;
    /**
     * 交易日
     */
    private Date tradingDate;
    /**
     * 涨跌幅 %
     */
    private BigDecimal priceChangeRatio;
    /**
     * 涨跌
     */
    private BigDecimal priceChange;

    /**
     * 换手率
     */
    private BigDecimal turnoverRate;

    /**
     * 振幅
     */
    private BigDecimal amplitude;



}
