package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.dto.ShareInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface ShareInfoMapper {
    int deleteByPrimaryKey(Integer idShareInfo);

    int insert(ShareInfoDto record);

    int insertSelective(ShareInfoDto record);

    ShareInfoDto selectByPrimaryKey(Integer idShareInfo);

    ShareInfoDto selectSelective(ShareInfoDto record);

    int updateByPrimaryKeySelective(ShareInfoDto record);

    int updateByPrimaryKey(ShareInfoDto record);
}