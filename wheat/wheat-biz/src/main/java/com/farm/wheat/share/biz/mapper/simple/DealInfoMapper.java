package com.farm.wheat.share.biz.mapper.simple;


import com.farm.wheat.share.biz.po.DealInfoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DealInfoMapper {
    int deleteByPrimaryKey(Integer idDealInfo);

    int insert(DealInfoPO record);

    int insertSelective(DealInfoPO record);

    DealInfoPO selectByPrimaryKey(Integer idDealInfo);

    int updateByPrimaryKeySelective(DealInfoPO record);

    int updateByPrimaryKeyWithBLOBs(DealInfoPO record);

    int updateByPrimaryKey(DealInfoPO record);
}