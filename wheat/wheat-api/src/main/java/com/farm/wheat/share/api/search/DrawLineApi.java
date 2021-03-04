package com.farm.wheat.share.api.search;

import com.farm.wheat.share.api.vo.DrawLineVO;
import com.farm.wheat.share.service.vo.DrawLineData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 */
@Api(description = "画线相关")
public interface DrawLineApi {
    @ApiOperation(value = "获取股票所有价格", notes = "获取股票所有价格")
    DrawLineData sharePrices(DrawLineVO drawLineVO) throws Exception;
}
