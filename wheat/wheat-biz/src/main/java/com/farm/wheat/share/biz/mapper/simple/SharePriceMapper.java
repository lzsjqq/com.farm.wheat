package com.farm.wheat.share.biz.mapper.simple;

import com.farm.wheat.share.biz.dto.SharePriceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 18:47
 */
@Mapper
public interface SharePriceMapper {

    int deleteByPrimaryKey(Integer idShareInfo);

    int insert(SharePriceDto record);

    int insertSelective(SharePriceDto record);

    SharePriceDto selectByPrimaryKey(Integer idShareInfo);

    int updateByPrimaryKeySelective(SharePriceDto record);

    int updateByPrimaryKey(SharePriceDto record);

    /**
     *
     * @param idShareInfo
     * @param tradingDate
     * @return
     */
    SharePriceDto selectByIdShareInfoAndTradingDate(@Param("idShareInfo") Integer idShareInfo,@Param("tradingDate")  Date tradingDate);
}
