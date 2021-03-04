package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.dto.ShareInfoDto;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    ShareInfoDto selectByShareCode(@Param("shareCode") String shareCode);

    Page<ShareInfoDto> selectShareInfoByPage(ShareInfoDto record);

    List<ShareInfoDto> selectAll();

    /**
     * 查询当前股票的数量
     *
     * @return
     */
    int countShareInfo();
}
