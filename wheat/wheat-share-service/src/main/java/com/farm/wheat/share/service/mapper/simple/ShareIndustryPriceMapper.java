package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.vo.ShareIndustryPriceVO;
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
