package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.dto.ShareConceptPriceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ShareConceptPriceMapper {
    int deleteByPrimaryKey(Integer idConceptPrice);

    int insert(ShareConceptPriceDTO record);

    int insertSelective(ShareConceptPriceDTO record);

    ShareConceptPriceDTO selectByPrimaryKey(Integer idConceptPrice);

    int updateByPrimaryKeySelective(ShareConceptPriceDTO record);

    int updateByPrimaryKey(ShareConceptPriceDTO record);

    int replaceSelective(ShareConceptPriceDTO shareConceptPriceDTO);

    int replaceList(@Param("list") List<ShareConceptPriceDTO> list);
}
