package com.farm.wheat.share.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "DealDetailInfoVO", description = "DealDetailInfoVO")
public class DealDetailInfoVO {
    @ApiModelProperty("交易日")
    private Date tradingDate;

    private String shareCode;

    private String shareName;

    private BigDecimal dealPrice;

    private Integer volume;
    @ApiModelProperty("1=买入 2=卖出")
    private String target;

    private BigDecimal stopLossPrice;

    private BigDecimal lowPrice;

    private BigDecimal highPrice;

    private BigDecimal profit;

    private BigDecimal fiveLowPrice;

    private BigDecimal fiveHighPrice;

    private BigDecimal fiveProfit;

    private BigDecimal tenLowPrice;

    private BigDecimal tenHighPrice;

    private BigDecimal tenProfit;

    private BigDecimal rRate;

    private String reason;

    @ApiModelProperty("1=继续持仓 2=完成交易")
    private Integer status;

}