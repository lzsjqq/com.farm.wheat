package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShareConceptPriceDTO {
    private Integer idConceptPrice;

    private String simpleName;

    private BigDecimal avgPrice;

    private BigDecimal change;

    private BigDecimal priceChangeRatio;

    private BigDecimal tradingMoney;

    private BigDecimal tradingVolume;

    private Date tradingDate;

    private String ledShareCode;

    private Date createTime;

    private Date updateTime;


}
