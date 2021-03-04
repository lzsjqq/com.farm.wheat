package com.farm.wheat.share.service.po;

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

    private BigDecimal stampDuty;

    private BigDecimal changeMoney;

    private Integer volume;

    private String target;

    private BigDecimal stopLossPrice;

    private Date createTime;

    private Date updateTime;

    private String plan;

}