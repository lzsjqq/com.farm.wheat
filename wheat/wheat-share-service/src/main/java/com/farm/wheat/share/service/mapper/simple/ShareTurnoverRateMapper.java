package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.dto.ShareTurnoverRateDTO;
import com.farm.wheat.share.service.vo.ShareTurnoverRateVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-14 09:56
 */
@Mapper
public interface ShareTurnoverRateMapper {
    int deleteByPrimaryKey(Integer idTurnoverRate);

    int insert(ShareTurnoverRateVO record);

    int insertSelective(ShareTurnoverRateVO record);

    ShareTurnoverRateVO selectByPrimaryKey(Integer idTurnoverRate);

    int updateByPrimaryKeySelective(ShareTurnoverRateVO record);

    int updateByPrimaryKey(ShareTurnoverRateVO record);

    Page<ShareTurnoverRateDTO> selectMaxChangeShares(int dayNum);
}
