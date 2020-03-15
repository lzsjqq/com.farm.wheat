package com.farm.wheat.share.chan.util;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: wheat
 * @description: 表示一根k线
 * @author: xyc
 * @create: 2020-03-13 21:46
 */
@Data
@NoArgsConstructor
public class Price {
    @JSONField(format = "yyyy-MM-dd")
    private String tradingDate;

    private double todayOpenPrice;
    /**
     * 今日收盘价
     */
    private double todayEndPrice;

    private double todayMaxPrice;

    private double todayMinPrice;

    private PriceRunTypeEnum priceRunType;
    private PriceTypeEnum priceType = PriceTypeEnum.NONE;
    /**
     * 包含之后的K线
     */
    private Price containPrice;
    /**
     * 一笔中的数量
     */
    private int biSize = 1;

    private int containSize = 1;

}