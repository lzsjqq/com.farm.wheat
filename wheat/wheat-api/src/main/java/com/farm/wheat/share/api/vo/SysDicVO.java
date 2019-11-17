package com.farm.wheat.share.api.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SysDicVO {

    private String dicCode;

    private String dicName;

    private String codeValue;

    private String codeNameCn;

    private String codeNameEn;

    private Date createTime;

    private Date updateTime;

}