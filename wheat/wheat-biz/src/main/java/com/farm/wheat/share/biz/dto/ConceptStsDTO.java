package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.util.List;

@Data
public class ConceptStsDTO {
    private Integer count;

    private String type;

    private List<ConceptPriceDTO> detail;
}
