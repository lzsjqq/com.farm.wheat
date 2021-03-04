package com.farm.wheat.share.service.mapper.simple;
import com.farm.wheat.share.service.dto.ShareConceptStsCountDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareConceptStsCountMapper {
    int insert(ShareConceptStsCountDTO record);

    int insertSelective(ShareConceptStsCountDTO record);
}