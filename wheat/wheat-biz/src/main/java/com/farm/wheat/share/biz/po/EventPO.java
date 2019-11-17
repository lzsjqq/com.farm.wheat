package com.farm.wheat.share.biz.po;

import lombok.Data;

import java.util.Date;
@Data
public class EventPO {

    private Integer idEvent;

    private Integer frequency;
    private String event;

    private String affect;

    private String type;
    private Date eventDate;

    private Date createTime;

    private Date updateTime;


}