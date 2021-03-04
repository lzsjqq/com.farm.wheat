package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.po.SysDicPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysDicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDicPO record);

    int insertSelective(SysDicPO record);

    SysDicPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDicPO record);

    int updateByPrimaryKey(SysDicPO record);

    SysDicPO dicOne(@Param("dicCode") String dicCode);
}