package com.farm.wheat.share.api.deal;

import com.farm.wheat.share.api.vo.*;
import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.SysDicPO;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "CommonApi")
public interface CommonApi {

    @ApiOperation(value = "字典项查询", notes = "字典项查询")
    SysDicPO dicOne(String  dicCode);



}
