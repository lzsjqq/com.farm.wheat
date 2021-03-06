package com.farm.wheat.share.chan.dto;

import com.farm.wheat.share.chan.util.KLineTypeEnum;
import com.farm.wheat.share.chan.util.RunTypeEnum;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 处理包含后的K线
 * @author: xyc
 * @create: 2021-03-06 12:23
 */
@Data
public class ContainedKLine {


    public ContainedKLine() {

    }

    public ContainedKLine(double openPrice, double minPrice, double maxPrice, int fromIndex, KLineTypeEnum lineTypeEnum) {
        this.openPrice = openPrice;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.fromIndex = fromIndex;
        this.minIndex = fromIndex;
        this.maxIndex = fromIndex;
        this.lineTypeEnum = lineTypeEnum;
    }

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
     * 后一根K线相较前一根K线的运行关系，
     * UP：前一根的K线高点和低点都比后一个K线的高点和低点底
     * DOWN：前一根的K线高点和低点都比后一个K线的高点和低点高
     * NONE：前面无K线或和前一根是包含关系
     */
    private RunTypeEnum runType;

    /**
     * k线类型
     */
    private KLineTypeEnum lineTypeEnum;

    /**
     * 包含K线的起始位置
     */
    private int fromIndex;

    /**
     * 包含K线的结束位置
     */
    private int endIndex;

    /**
     * minPrice的位置
     */
    private int minIndex;

    /**
     * maxPrice的位置
     */
    private int maxIndex;


    public void setMinPrice(double minPrice, int minIndex) {
        if (this.minPrice > minPrice) {
            this.minIndex = minIndex;
        }
        this.minPrice = minPrice;
    }

    public void setMaxPrice(double maxPrice, int maxIndex) {
        if (this.maxPrice < maxPrice) {
            this.maxIndex = maxIndex;
        }
        this.maxPrice = maxPrice;
    }


}