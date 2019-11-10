package com.farm.wheat.share.biz.service.deal;

import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.mapper.simple.DealInfoMapper;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.github.pagehelper.PageInfo;

/**
 * 交易记录相关
 */
public interface DealService {



    PageInfo<DealInfoPO> dealInfo(DealInfoDTO dealInfoDTO);
}
