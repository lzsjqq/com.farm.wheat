package com.farm.wheat.share.service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShareInfoDto extends EntityDto {
    private Integer idShareInfo;

    private String shareCode;

    private String shareName;


    private BigDecimal circulationMarketValue;

    private BigDecimal totalMarketValue;

    private Integer source;

    private String industry;


}