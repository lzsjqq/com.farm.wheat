package com.farm.wheat.share.chan.util;

import lombok.Data;

/**
 * @description: 三个元素
 * @author: xyc
 * @create: 2020-03-27 01:14
 */
@Data
public class Triple<F, S, T> {

    private F first;
    private S second;
    private T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Triple() {
    }
}