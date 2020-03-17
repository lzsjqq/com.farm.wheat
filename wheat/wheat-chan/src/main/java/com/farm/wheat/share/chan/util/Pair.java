package com.farm.wheat.share.chan.util;

import lombok.Data;

/**
 * @description: 两个值
 * @author: xyc
 * @create: 2020-03-14 20:11
 */
@Data
public class Pair<F, S> {

    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {
    }
}