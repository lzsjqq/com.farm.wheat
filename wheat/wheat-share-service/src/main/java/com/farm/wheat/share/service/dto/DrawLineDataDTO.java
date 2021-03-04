package com.farm.wheat.share.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrawLineDataDTO {
    private List<SharePriceDto> sharePrices;
}
