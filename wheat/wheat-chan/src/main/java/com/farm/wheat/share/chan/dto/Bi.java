package com.farm.wheat.share.chan.dto;

import java.math.BigDecimal;

/**
 * @description: 笔
 * @author: xyc
 * @create: 2021-03-05 00:08
 */

public class Bi {
    /**
     * 最高价
     */
    private BigDecimal todayMaxPrice;
    /**
     * 最低价
     */
    private BigDecimal todayMinPrice;


    /**
     * B开始的分型
     */
    private FenXing start;

    /**
     * B结束的分型
     */
    private FenXing end;


    /**
     * 上一笔
     */
    private Bi last;

    /**
     * 下一笔
     */
    private Bi next;
}