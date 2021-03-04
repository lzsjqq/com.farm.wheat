package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.dto.ShareConceptInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShareConceptInfoMapper {
    int deleteByPrimaryKey(Integer idConceptInfo);

    int insert(ShareConceptInfoDTO record);

    int insertSelective(ShareConceptInfoDTO record);

    int insertList(@Param("list") List<ShareConceptInfoDTO> list);


    int replaceList(@Param("list") List<ShareConceptInfoDTO> list);

    ShareConceptInfoDTO selectByPrimaryKey(Integer idConceptInfo);

    List<ShareConceptInfoDTO> selectAll();

    int updateByPrimaryKeySelective(ShareConceptInfoDTO record);

    int updateByPrimaryKeyWithBLOBs(ShareConceptInfoDTO record);

    int updateByPrimaryKey(ShareConceptInfoDTO record);

    int replaceSelective(ShareConceptInfoDTO conceptInfoDTO);
}
