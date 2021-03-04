package com.farm.wheat.share.api.deal;

import com.farm.wheat.share.service.po.SysDicPO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "CommonApi")
public interface CommonApi {

    @ApiOperation(value = "字典项查询", notes = "字典项查询")
    SysDicPO dicOne(String  dicCode);



}
