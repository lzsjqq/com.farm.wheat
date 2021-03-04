package com.farm.wheat.share.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DealDetailInfoDTO extends EntityDto {
    private Integer idDealDetailInfo;

    private Integer idDealInfo;

    private String tradingDate;

    private String shareCode;

    private String shareName;

    private BigDecimal dealPrice;

    private Integer volume;

    private DealTargetEnum target;

    private BigDecimal stopLossPrice;

    private Date createTime;

    private Date updateTime;

    private String plan;

    private Integer status;

    private BigDecimal stampDuty;

    private BigDecimal changeMoney;
}