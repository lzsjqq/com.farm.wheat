package com.farm.wheat.share.biz.mapper.simple;


import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DealInfoMapper {
    int deleteByPrimaryKey(Integer idDealInfo);

    int insert(DealInfoPO record);

    int insertSelective(DealInfoPO record);

    DealInfoPO selectByPrimaryKey(Integer idDealInfo);

    Page<DealInfoPO>  selectBySelective(DealInfoDTO dealInfoDTO);

    int updateByPrimaryKeySelective(DealInfoPO record);

    int updateByPrimaryKeyWithBLOBs(DealInfoPO record);

    int updateByPrimaryKey(DealInfoPO record);
}