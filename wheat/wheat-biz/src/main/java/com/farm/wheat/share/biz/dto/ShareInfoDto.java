package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ShareInfoDto  extends  EntityDto{
    private Integer idShareInfo;

    private String shareCode;

    private String shareName;

    private BigDecimal peRatio;

    private BigDecimal pbRatio;

    private BigDecimal circulationMarketValue;

    private BigDecimal totalMarketValue;

    private Integer source;




}