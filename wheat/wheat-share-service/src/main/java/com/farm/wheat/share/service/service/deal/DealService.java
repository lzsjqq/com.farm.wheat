package com.farm.wheat.share.service.service.deal;

import com.farm.wheat.share.service.dto.DealDetailInfoDTO;
import com.farm.wheat.share.service.dto.DealInfoDTO;
import com.farm.wheat.share.service.dto.EntityDto;
import com.farm.wheat.share.service.po.DealDetailInfoPO;
import com.farm.wheat.share.service.po.DealInfoPO;
import com.farm.wheat.share.service.po.EventPO;
import com.github.pagehelper.PageInfo;

/**
 * 交易记录相关
 */
public interface DealService {


    PageInfo<DealInfoPO> dealInfo(DealInfoDTO dealInfoDTO);

    /**
     * 插入交易明细
     *
     * @param dealDetailInfoDTO
     * @return
     */
    int insertDetail(DealDetailInfoDTO dealDetailInfoDTO) throws Exception;

    /**
     * 明细
     *
     * @param record
     * @return
     */
    PageInfo<DealDetailInfoPO> dealDetailInfo(DealDetailInfoDTO record);

    /**
     * 完成交易了
     *
     * @param shareCode
     */
    void completeDeal(String shareCode) throws Exception;

    /**
     * 插入事件
     *
     * @param convert
     */
    void insertEvent(EventPO convert);

    /**
     * 更新
     *
     * @param dealInfoDTO
     */
    void updateDealInfo(DealInfoDTO dealInfoDTO) throws Exception;

    PageInfo<EventPO> listEvents(EntityDto record);
}
