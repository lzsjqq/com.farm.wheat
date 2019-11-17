package com.farm.wheat.share.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "EventVO", description = "EventVO")
public class EventVO {
    @ApiModelProperty("最近提及次数")
    private Integer frequency;
    @ApiModelProperty("事件描述")
    private String event;
    @ApiModelProperty("事件对股市影响")
    private String affect;
    @ApiModelProperty("事件类型")
    private String type;
    @ApiModelProperty("事件发生日")
    private Date eventDate;
}
