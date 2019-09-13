package com.farm.wheat.share.biz.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

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
    private BigDecimal peRatio;
    /**
     * 市净率
     */
    private BigDecimal pbRatio;
    /**
     * 流通市值
     */
    private BigDecimal circulationMarketValue;
    /**
     * 总市值
     */
    private BigDecimal totalMarketValue;

    private Integer source;


    private String createBy;

    private String updateBy;
}
