package com.farm.wheat.share.chan.dto;

import com.farm.wheat.share.chan.util.FengXingTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 分型定义
 * 顶分型定义：不含包含关系的3根K线，中间一根的高点最高，低点也最高；
 * 底分型定义：不含包含关系的3根K线，中间一根的低点最低，高点也最低；
 * @author: xyc
 * @create: 2021-03-04 23:57
 */
@Data
public class FenXing {

    /**
     * 是否是分型
     */
    private FengXingTypeEnum fenXingTypeEnum = FengXingTypeEnum.NONE;
    /**
     * 最高价
     */
    private double maxPrice;
    /**
     * 最低价
     */
    private double minPrice;

    /**
     * 第一根K线
     */
    private ContainedKLine startLine;

    /**
     * 第二根K线
     */
    private ContainedKLine middleLine;

    /**
     * 第三根K线
     */
    private ContainedKLine endLine;




    /**
     * 上一个分型
     */
    private FenXing last;

    /**
     * 下一个分型
     */
    private FenXing next;


}