package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.vo.ShareIndustryDetailVO;
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
