package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.vo.ShareIndustryPriceVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareIndustryPriceMapper {
    int deleteByPrimaryKey(Integer idIndustryPrice);

    int insert(ShareIndustryPriceVO record);

    int insertSelective(ShareIndustryPriceVO record);

    ShareIndustryPriceVO selectByPrimaryKey(Integer idIndustryPrice);

    int updateByPrimaryKeySelective(ShareIndustryPriceVO record);

    int updateByPrimaryKey(ShareIndustryPriceVO record);
}
