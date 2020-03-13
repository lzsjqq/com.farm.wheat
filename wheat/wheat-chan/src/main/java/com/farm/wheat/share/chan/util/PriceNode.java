package com.farm.wheat.share.chan.util;

import lombok.Data;

/**
 * @program: wheat
 * @description: 表示一根k线
 * @author: xyc
 * @create: 2020-03-13 21:46
 */
@Data
public class PriceNode {
    /**
     * 上一个节点
     */
    private PriceNode preNode;
    /**
     * 下一个节点
     */
    private PriceNode nextNode;


}