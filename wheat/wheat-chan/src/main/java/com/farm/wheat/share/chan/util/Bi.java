package com.farm.wheat.share.chan.util;

import com.farm.wheat.share.chan.dto.KLine;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 笔
 * @author: xyc
 * @create: 2020-03-23 23:12
 */
@Data
@NoArgsConstructor
public class Bi {
    /**
     * 笔分型的开始
     */
    private Integer fromIndex;
    /**
     * 笔分型的结束
     */
    private Integer toIndex;
    /**
     * 笔分型的开始
     */
    private KLine fromKLine;
    /**
     * 笔分型的结束
     */
    private KLine toKLine;
}