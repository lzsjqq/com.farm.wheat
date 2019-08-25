package com.farm.wheat.share.biz.dto;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 23:18
 */
@Data
public class EntityDto {

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;
}
