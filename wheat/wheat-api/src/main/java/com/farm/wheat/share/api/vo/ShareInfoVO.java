package com.farm.wheat.share.api.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-07 14:48
 */
@Data
public class ShareInfoVO  extends EntityVO{
    private Integer idShareInfo;

    private String shareCode;

    private String shareName;

    private BigDecimal circulationMarketValue;

    private BigDecimal totalMarketValue;

    private Integer source;

    private String industry;
}
