package com.farm.wheat.share.biz.mapper.max;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-10-27 21:32
 */
@Mapper
public interface ConceptStsMapper {


    /**
     * 统计并插入数据
     *
     * @param data
     * @param limit
     * @param type
     */
    void stsTopRatio(@Param("date") Date data, @Param("limit") int limit, @Param("type") String type);
}
