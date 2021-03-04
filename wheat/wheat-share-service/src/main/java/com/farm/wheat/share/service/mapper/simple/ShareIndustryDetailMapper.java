package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.vo.ShareIndustryDetailVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareIndustryDetailMapper {
    int deleteByPrimaryKey(Integer idIndustryDetail);

    int insert(ShareIndustryDetailVO record);

    int insertSelective(ShareIndustryDetailVO record);

    ShareIndustryDetailVO selectByPrimaryKey(Integer idIndustryDetail);

    int updateByPrimaryKeySelective(ShareIndustryDetailVO record);

    int updateByPrimaryKey(ShareIndustryDetailVO record);
}
