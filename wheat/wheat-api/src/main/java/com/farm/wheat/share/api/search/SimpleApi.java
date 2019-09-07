package com.farm.wheat.share.api.search;

import com.farm.wheat.share.api.vo.ShareInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-07 14:31
 */
@Api(description = "简单搜索")
public interface SimpleApi {

    @ApiOperation(value = "获取股票基本信息", notes = "获取股票基本信息")
    List<ShareInfoVO> getShares();
}
