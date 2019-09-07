package com.farm.wheat.share.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 23:18
 */
@Data
public class EntityVO {

    private Date createTime;

    private Date updateTime;

    private String createBy;

    private String updateBy;
}
