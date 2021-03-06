package com.farm.wheat.share.chan.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.farm.wheat.share.chan.util.KTypeEnum;
import com.farm.wheat.share.chan.util.RunTypeEnum;
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


    /**
     * 后一根K线相较前一根K线的运行关系，
     * UP：前一根的K线高点和低点都比后一个K线的高点和低点底
     * DOWN：前一根的K线高点和低点都比后一个K线的高点和低点高
     * NONE：前面无K线或和前一根是包含关系
     */
    private RunTypeEnum runType;

    /**
     * 是否是分型
     */
    private KTypeEnum priceType = KTypeEnum.NONE;
    /**
     * 包含之后的K线
     */
    private KLine containKLine;
    /**
     * 包含之后的K线
     */
    private ContainedKLine containedKLine;

    /**
     * 开盘价
     */
    private double openPrice;
    /**
     * 今日收盘价
     */
    private double endPrice;
    /**
     * 最高价
     */
    private double maxPrice;
    /**
     * 最低价
     */
    private double minPrice;


    /**
     * 两个底分型之间的K线数量，0-1 1-2 2-3
     */
    private int fromToSize = 1;

    private int containSize = 1;

}