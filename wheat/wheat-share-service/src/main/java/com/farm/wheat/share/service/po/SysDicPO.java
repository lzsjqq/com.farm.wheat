package com.farm.wheat.share.service.po;

import lombok.Data;

import java.util.Date;
@Data
public class SysDicPO {
    private Integer id;

    private String dicCode;

    private String dicName;

    private String codeValue;

    private String codeNameCn;

    private String codeNameEn;

    private Date createTime;

    private Date updateTime;

}