package com.farm.wheat.share.biz.service.statistics.impl;

import com.farm.common.utils.DateUtils;
import com.farm.wheat.share.biz.mapper.max.ConceptStsMapper;
import com.farm.wheat.share.biz.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.biz.service.statistics.IConceptSts;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-10-27 21:30
 */
@Service
public class ConceptStsImpl implements IConceptSts {

    @Resource
    private ShareInfoMapper shareInfoMapper;
    @Resource
    private ConceptStsMapper conceptStatisticsMapper;

    @Override
    public void stsTopRatio(String date, int type) throws Exception {
        int shareCount = shareInfoMapper.countShareInfo();
        int limit = shareCount * type / 10;
        Date target = DateUtils.stringToDate(date, DateUtils.YYYY_MM_DD);
        conceptStatisticsMapper.stsTopRatio(target, limit, String.valueOf(type));
    }
}
