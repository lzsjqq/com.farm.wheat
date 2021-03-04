package com.farm.wheat.share.service.service.statistics;

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
     */
    void stsTopRatio(String date) throws Exception;
}
