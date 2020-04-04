package com.farm.wheat.share.chan.util;

import com.farm.common.utils.Linked;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 用于描述一段
 * @author: xyc
 * @create: 2020-03-23 21:03
 */
@Data
public class Segment {
    /**
     * 笔分型的开始index
     */
    private Integer fromIndex;
    /**
     * 笔分型的结束index
     */
    private Integer toIndex;

    /**
     * 描述未成形笔分型，段之间
     */
    private Linked<Price> biPrices = new Linked<>();

}