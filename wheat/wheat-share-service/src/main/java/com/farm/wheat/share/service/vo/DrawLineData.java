package com.farm.wheat.share.service.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 画线数据所需数据
 * @author: xyc
 * @create: 2020-03-16 00:10
 */
@Data
public class DrawLineData {
    /**
     * K线基本数据，数据意义：2013/1/24 ,开盘(open)，收盘(close)，最低(lowest)，最高(highest),成交量
     */
    private List<Object[]> baseData;
    private List<String> priceType;

}