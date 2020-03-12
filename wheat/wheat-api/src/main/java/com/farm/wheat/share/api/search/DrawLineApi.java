package com.farm.wheat.share.api.search;

import com.farm.wheat.share.api.vo.DrawLineVO;
import com.farm.wheat.share.api.vo.ShareInfoVO;
import com.farm.wheat.share.api.vo.request.SharesReq;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 *
 */
@Api(description = "画线相关")
public interface DrawLineApi {
    @ApiOperation(value = "获取股票所有价格", notes = "获取股票所有价格")
    List<Object[]> sharePrices(DrawLineVO drawLineVO) throws Exception;
}
