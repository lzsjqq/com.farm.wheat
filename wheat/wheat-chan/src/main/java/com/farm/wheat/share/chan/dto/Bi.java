package com.farm.wheat.share.chan.dto;

import com.farm.wheat.share.chan.util.BiRunTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 笔
 * @author: xyc
 * @create: 2021-03-05 00:08
 */
@Data
public class Bi {
    /**
     * 最高价
     */
    private BigDecimal mxPrice;
    /**
     * 最低价
     */
    private BigDecimal minPrice;

    /**
     * 笔的运行方向，
     * UP=由底分型开始，
     * DOWN=由底分型开始
     */
    private BiRunTypeEnum biRunTypeEnum;

    /**
     * B开始的分型
     */
    private FenXing startFenXing;

    /**
     * 分型之间的K线
     */
    private List<ContainedKLine> containedKLineList;

    /**
     * B结束的分型
     */
    private FenXing endFenXing;


}