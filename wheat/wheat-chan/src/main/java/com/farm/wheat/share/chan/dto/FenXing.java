package com.farm.wheat.share.chan.dto;

import com.farm.wheat.share.chan.util.KTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 分型定义
 * @author: xyc
 * @create: 2021-03-04 23:57
 */
@Data
public class FenXing {
    /**
     * 最高价
     */
    private BigDecimal todayMaxPrice;
    /**
     * 最低价
     */
    private BigDecimal todayMinPrice;

    /**
     * 第一根K线
     */
    private KLine start;

    /**
     * 第二根K线
     */
    private KLine middle;

    /**
     * 第三根K线
     */
    private FenXing end;


    /**
     * 上一个分型
     */
    private FenXing last;

    /**
     * 下一个分型
     */
    private FenXing next;


}