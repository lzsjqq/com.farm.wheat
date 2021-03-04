package com.farm.wheat.share.service.mapper.simple;

import com.farm.wheat.share.service.dto.SharePriceDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

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
     * @param idShareInfo
     * @param tradingDate
     * @return
     */
    SharePriceDto selectByIdShareInfoAndTradingDate(@Param("idShareInfo") Integer idShareInfo, @Param("tradingDate") Date tradingDate);

    List<SharePriceDto> selectByIdShareInfo(@Param("idShareInfo") Integer idShareInfo, @Param("limit") int limit);

    /**
     * 查询历史数据
     *
     * @param shareCode
     * @return
     */
    List<SharePriceDto> selectSharePrices(@Param("shareCode") String shareCode);
}
