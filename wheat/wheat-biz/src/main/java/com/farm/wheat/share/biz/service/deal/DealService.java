package com.farm.wheat.share.biz.service.deal;

import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.mapper.simple.DealInfoMapper;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.github.pagehelper.PageInfo;

/**
 * 交易记录相关
 */
public interface DealService {


    PageInfo<DealInfoPO> dealInfo(DealInfoDTO dealInfoDTO);

    /**
     * 插入交易明细
     *
     * @param dealDetailInfoPO
     * @return
     */
    int insertDetail(DealDetailInfoPO dealDetailInfoPO) throws Exception;

    /**
     * 明细
     *
     * @param record
     * @return
     */
    PageInfo<DealDetailInfoPO> dealDetailInfo(DealDetailInfoDTO record);
}
