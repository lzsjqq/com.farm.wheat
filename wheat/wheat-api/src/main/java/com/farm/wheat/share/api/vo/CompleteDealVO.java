package com.farm.wheat.share.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "完成操作", description = "完成操作")
public class CompleteDealVO {

    @ApiModelProperty("股票代码")
    private String shareCode;
}