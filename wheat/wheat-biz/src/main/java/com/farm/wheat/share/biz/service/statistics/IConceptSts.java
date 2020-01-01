package com.farm.wheat.share.biz.service.statistics;

import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-10-27 21:30
 */
public interface IConceptSts {

    /**
     * 根据比例统计数据
     *
     * @param date  那一天
     * @param ratio 前十分之几
     */
    void stsTopRatio(String date, int ratio) throws Exception;
}
