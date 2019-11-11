package com.farm.wheat.share.api.deal;

import com.farm.wheat.share.api.vo.DealDetailInfoVO;
import com.farm.wheat.share.api.vo.DealInfoVO;
import com.farm.wheat.share.api.vo.ShareInfoVO;
import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "DealApi")
public interface DealApi {

    @ApiOperation(value = "交易记录分页查询", notes = "交易记录分页查询")
    PageInfo<DealInfoVO> dealInfo(DealInfoDTO dealInfoDTO);

    @ApiOperation(value = "交易明细分页查询", notes = "交易明细分页查询")
    PageInfo<DealDetailInfoPO> dealDetailInfo( DealDetailInfoDTO dealDetailInfoDTO);

    @ApiOperation(value = "插入交易明细", notes = "插入交易明细")
    int insertDetail(DealDetailInfoVO dealDetailInfoVO) throws Exception;
}
