package com.farm.wheat.share.dto;


import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-24 23:11
 */
@Data
public class ShareBaseInfoDTO {

    /**
     * 股票代码
     */
    private String shareCode;
    /**
     * 名字
     */
    private String shareName;

    /**
     * 市盈率
     */
    private BigDecimal PERatio;
    /**
     * 市净率
     */
    private BigDecimal PBRatio;
    /**
     * 流通市值
     */
    private BigDecimal circulationMarketValue;
    /**
     * 总市值
     */
    private BigDecimal totalMarketValue;


}
