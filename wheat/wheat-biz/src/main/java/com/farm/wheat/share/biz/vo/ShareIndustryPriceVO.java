package com.farm.wheat.share.biz.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShareIndustryPriceVO {
    private Integer idIndustryPrice;

    private Integer idIndustryInfo;

    private BigDecimal avgPrice;

    private BigDecimal change;

    private BigDecimal priceChangeRatio;

    private BigDecimal tradingMoney;

    private BigDecimal tradingVolume;

    private String ledShareCode;

    private Date createTime;

    private Date updateTime;


}
