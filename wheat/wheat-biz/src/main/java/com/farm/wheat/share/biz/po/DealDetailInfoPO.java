package com.farm.wheat.share.biz.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DealDetailInfoPO {
    private Integer idDealDetailInfo;

    private Integer idDealInfo;

    private Date tradingDate;

    private String shareCode;

    private String shareName;

    private BigDecimal dealPrice;

    private Integer volume;

    private String target;

    private BigDecimal stopLossPrice;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private BigDecimal profit;

    private BigDecimal fiveLowPrice;

    private BigDecimal fiveHighPrice;

    private BigDecimal fiveProfit;

    private BigDecimal tenLowPrice;

    private BigDecimal tenHighPrice;

    private BigDecimal tenProfit;

    private BigDecimal rRate;

    private Date createTime;

    private Date updateTime;

    private String plan;

}