package com.farm.wheat.share.service.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ShareIndustryDetailVO {
    private Integer idIndustryDetail;

    private Integer idShareInfo;

    private String shareCode;

    private String shareName;

    private Date createTime;

    private Date updateTime;


}
