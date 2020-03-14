package com.farm.wheat.share.api.chan;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = {"构造缠论结构"})
public interface ChanApi {

    @ApiOperation(value = "构造缠论结构", notes = "构造缠论结构")
    void build(String shareCode);
}
