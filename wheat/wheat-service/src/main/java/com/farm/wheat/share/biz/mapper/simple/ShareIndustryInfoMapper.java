package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.vo.ShareIndustryInfoVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareIndustryInfoMapper {
    int deleteByPrimaryKey(Integer idIndustryInfo);

    int insert(ShareIndustryInfoVO record);

    int insertSelective(ShareIndustryInfoVO record);

    ShareIndustryInfoVO selectByPrimaryKey(Integer idIndustryInfo);

    int updateByPrimaryKeySelective(ShareIndustryInfoVO record);

    int updateByPrimaryKeyWithBLOBs(ShareIndustryInfoVO record);

    int updateByPrimaryKey(ShareIndustryInfoVO record);
}
