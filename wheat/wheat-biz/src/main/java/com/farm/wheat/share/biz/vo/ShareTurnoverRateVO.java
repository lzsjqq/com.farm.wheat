package com.farm.wheat.share.biz.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-13 23:41
 */
@Data
public class ShareTurnoverRateVO {
    private Integer idTurnoverRate;
    private Integer idShareInfo;
    private BigDecimal newRate;
    private BigDecimal threeRate;
    private BigDecimal threeChangeRate;
    private BigDecimal tenChangeRate;
    private BigDecimal fiveChangeRate;
    private BigDecimal tenRate;
    private BigDecimal fiveRate;
    private Date createTime;
    private Date updateTime;
}
