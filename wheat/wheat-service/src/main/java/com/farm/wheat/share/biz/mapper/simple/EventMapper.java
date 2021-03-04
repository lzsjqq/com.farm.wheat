package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.po.EventPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    int deleteByPrimaryKey(Integer idEvent);

    int insert(EventPO record);

    int insertSelective(EventPO record);

    EventPO selectByPrimaryKey(Integer idEvent);

    int updateByPrimaryKeySelective(EventPO record);

    int updateByPrimaryKeyWithBLOBs(EventPO record);

    int updateByPrimaryKey(EventPO record);

    List<EventPO> selectAll();
}