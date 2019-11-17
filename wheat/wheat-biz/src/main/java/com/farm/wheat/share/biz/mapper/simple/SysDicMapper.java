package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.po.SysDicPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysDicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDicPO record);

    int insertSelective(SysDicPO record);

    SysDicPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDicPO record);

    int updateByPrimaryKey(SysDicPO record);
}