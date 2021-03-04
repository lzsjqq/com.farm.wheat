package com.farm.wheat.share.chan.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.farm.wheat.share.chan.util.KTypeEnum;
import com.farm.wheat.share.chan.util.PriceRunTypeEnum;
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
public class KLine {
    @JSONField(format = "yyyy-MM-dd")
    private String tradingDate;
    /**
     * list中的角标
     */
    private Integer index;
    private PriceRunTypeEnum priceRunType;

    private KTypeEnum priceType = KTypeEnum.NONE;
    /**
     * 包含之后的K线
     */
    private KLine containKLine;


    private double todayOpenPrice;
    /**
     * 今日收盘价
     */
    private double todayEndPrice;

    private double todayMaxPrice;

    private double todayMinPrice;



    /**
     * 两个底分型之间的K线数量，0-1 1-2 2-3
     */
    private int fromToSize = 1;

    private int containSize = 1;

}