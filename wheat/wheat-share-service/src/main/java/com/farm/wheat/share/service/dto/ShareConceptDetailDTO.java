package com.farm.wheat.share.service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ShareConceptDetailDTO {
    private Integer idConceptDetail;

    private String simpleName;

    private String shareCode;

    private String shareName;

    private Date createTime;

    private Date updateTime;


}
