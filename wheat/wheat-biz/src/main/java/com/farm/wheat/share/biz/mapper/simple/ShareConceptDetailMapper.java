package com.farm.wheat.share.biz.mapper.simple;


import com.farm.wheat.share.biz.dto.ConceptPriceDTO;
import com.farm.wheat.share.biz.dto.ShareConceptDetailDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ShareConceptDetailMapper {
    int deleteByPrimaryKey(Integer idConceptDetail);

    /**
     * 按概念简称删除
     *
     * @param simpleName
     * @return
     */
    int deleteBySimpleName(@Param("simpleName") String simpleName);

    int insert(ShareConceptDetailDTO record);

    int insertSelective(ShareConceptDetailDTO record);

    int insertList(@Param("list") List<ShareConceptDetailDTO> list);

    ShareConceptDetailDTO selectByPrimaryKey(Integer idConceptDetail);

    int updateByPrimaryKeySelective(ShareConceptDetailDTO record);

    int updateByPrimaryKey(ShareConceptDetailDTO record);

    /**
     * 查询全部
     *
     * @return
     */
    List<ShareConceptDetailDTO> selectAll();

    List<ConceptPriceDTO> selectBySimpleName(@Param("simpleName")String simpleName, @Param("target") Date target);
}
