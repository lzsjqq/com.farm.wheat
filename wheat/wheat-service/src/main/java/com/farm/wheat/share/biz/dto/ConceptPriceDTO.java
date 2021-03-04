package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConceptPriceDTO {
    private BigDecimal priceChangeRatio;
    private String shareCode;
}
