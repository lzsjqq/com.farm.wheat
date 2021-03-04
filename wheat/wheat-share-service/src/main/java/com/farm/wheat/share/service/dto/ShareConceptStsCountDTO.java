package com.farm.wheat.share.service.dto;

import lombok.Data;

import java.util.Date;
@Data
public class ShareConceptStsCountDTO {
    private String simpleName;

    private String conceptName;

    private Date tradingDate;

    private Integer count;

    private Date createTime;

    private Date updateTime;

    private String detail;


}