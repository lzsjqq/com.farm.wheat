package main.java.com.farm.wheat.share.biz.mapper.simple;

import main.java.com.farm.wheat.share.biz.po.DealDetailInfo;

public interface DealDetailInfoMapper {
    int deleteByPrimaryKey(Integer idDealDetailInfo);

    int insert(DealDetailInfo record);

    int insertSelective(DealDetailInfo record);

    DealDetailInfo selectByPrimaryKey(Integer idDealDetailInfo);

    int updateByPrimaryKeySelective(DealDetailInfo record);

    int updateByPrimaryKeyWithBLOBs(DealDetailInfo record);

    int updateByPrimaryKey(DealDetailInfo record);
}