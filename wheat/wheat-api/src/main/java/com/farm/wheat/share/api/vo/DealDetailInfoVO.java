package com.farm.wheat.share.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "DealDetailInfoVO", description = "DealDetailInfoVO")
public class DealDetailInfoVO {
    @ApiModelProperty("交易日, 不填默认为当天")
    private String tradingDate;
    @ApiModelProperty("股票代码")
    private String shareCode;
    @ApiModelProperty("股票名")
    private String shareName;

    @ApiModelProperty("成交价")
    private BigDecimal dealPrice;
    @ApiModelProperty("成交量")
    private Integer volume;
    @ApiModelProperty("1=买入 2=卖出")
    private String target;

    @ApiModelProperty("止损价，不能超过10%")
    private BigDecimal stopLossPrice;
    @ApiModelProperty("买卖计划")
    private String plan;

}