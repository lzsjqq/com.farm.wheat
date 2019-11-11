package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DealDetailInfoMapper {
    int deleteByPrimaryKey(Integer idDealDetailInfo);

    int insert(DealDetailInfoPO record);

    int insertSelective(DealDetailInfoPO record);

    DealDetailInfoPO selectByPrimaryKey(Integer idDealDetailInfo);

    int updateByPrimaryKeySelective(DealDetailInfoPO record);

    int updateByPrimaryKeyWithBLOBs(DealDetailInfoPO record);

    int updateByPrimaryKey(DealDetailInfoPO record);

    Page<DealDetailInfoPO> selectBySelective(DealDetailInfoDTO record);
}