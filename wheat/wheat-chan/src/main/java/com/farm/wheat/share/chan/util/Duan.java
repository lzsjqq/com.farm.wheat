package com.farm.wheat.share.chan.util;

import lombok.Data;

/**
 * @description: 段
 * @author: xyc
 * @create: 2020-04-05 21:54
 */
@Data
public class Duan {
    private Integer from;
    private Integer to;
    private RunTypeEnum priceRunType;
}