package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShareInfoMapper {
    int deleteByPrimaryKey(Integer idShareInfo);

    int insert(ShareInfoDto record);

    int insertSelective(ShareInfoDto record);

    int replaceSelective(ShareInfoDto record);

    ShareInfoDto selectByPrimaryKey(Integer idShareInfo);

    ShareInfoDto selectSelective(ShareInfoDto record);

    int updateByPrimaryKeySelective(ShareInfoDto record);

    int updateByPrimaryKey(ShareInfoDto record);

    ShareInfoDto selectByShareCode(ShareInfoDto shareInfoDto);

    Page<ShareInfoDto> selectShareInfoByPage(ShareInfoDto record);
}
