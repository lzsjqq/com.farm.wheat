package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrawLineDataDTO {
    private List<SharePriceDto> sharePrices;
}
