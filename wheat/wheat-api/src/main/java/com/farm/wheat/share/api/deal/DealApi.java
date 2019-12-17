package com.farm.wheat.share.api.deal;

import com.farm.wheat.share.api.vo.CompleteDealVO;
import com.farm.wheat.share.api.vo.DealDetailInfoVO;
import com.farm.wheat.share.api.vo.DealInfoVO;
import com.farm.wheat.share.api.vo.EventVO;
import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.EventPO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@Api(description = "DealApi")
public interface DealApi {

    @ApiOperation(value = "交易记录分页查询", notes = "交易记录分页查询")
    PageInfo<DealInfoVO> dealInfo(DealInfoDTO dealInfoDTO);

    @ApiOperation(value = "交易明细分页查询", notes = "交易明细分页查询")
    PageInfo<DealDetailInfoPO> dealDetailInfo( DealDetailInfoDTO dealDetailInfoDTO);

    @ApiOperation(value = "插入交易明细", notes = "插入交易明细")
    int insertDetail(DealDetailInfoVO dealDetailInfoVO) throws Exception;

    @ApiOperation(value = "插入事件", notes = "插入事件")
    void insertEvent(EventVO eventVO) throws Exception;

    @ApiOperation(value = "列出事件", notes = "列出事件")
    List<EventPO> listEvents() throws Exception;

    @ApiOperation(value = "完成交易", notes = "完成交易")
    void completeDeal(CompleteDealVO completeDealVO) throws Exception;

    @ApiOperation(value = "更新", notes = "更新")
     void updateDealInfo(DealInfoDTO dealInfoDTO) throws Exception;

}
