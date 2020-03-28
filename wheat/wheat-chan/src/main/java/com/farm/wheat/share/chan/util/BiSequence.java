package com.farm.wheat.share.chan.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 笔的特征序列
 * @author: xyc
 * @create: 2020-03-18 21:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BiSequence {
    /**
     * 笔分型的开始时间
     */
    private String fromTradingDate;
    private Integer fromIndex;
    /**
     * 笔分型的结束时间
     */
    private String toTradingDate;
    private Integer toIndex;

    private double todayMaxPrice;

    private double todayMinPrice;

    private BiPriceTypeEnum priceType;

    private PriceRunTypeEnum priceRunType;
    /**
     * 包含之后的K线
     */
    private BiSequence containPrice;

    private int containSize = 1;


}