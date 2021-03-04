package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.dto.DealDetailInfoDTO;
import com.farm.wheat.share.service.po.DealDetailInfoPO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DealDetailInfoMapper {
    int deleteByPrimaryKey(Integer idDealDetailInfo);

    int insert(DealDetailInfoDTO record);

    int insertSelective(DealDetailInfoPO record);

    DealDetailInfoPO selectByPrimaryKey(Integer idDealDetailInfo);

    int updateByPrimaryKeySelective(DealDetailInfoPO record);

    int updateByPrimaryKeyWithBLOBs(DealDetailInfoPO record);

    int updateByPrimaryKey(DealDetailInfoPO record);

    Page<DealDetailInfoPO> selectBySelective(DealDetailInfoDTO record);
}