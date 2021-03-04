package com.farm.wheat.share.biz.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ShareIndustryInfoVO {
    private Integer idIndustryInfo;

    private String industryName;

    private Integer number;

    private Date createTime;

    private Date updateTime;

    private String desc;


}
