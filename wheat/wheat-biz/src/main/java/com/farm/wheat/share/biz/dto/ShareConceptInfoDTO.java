package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ShareConceptInfoDTO  {
    private String simpleName;

    private String conceptName;

    private Integer number;

    private Date createTime;

    private Date updateTime;

    private String desc;


}
