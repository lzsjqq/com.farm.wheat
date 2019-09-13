package com.farm.wheat.share.api.download;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-28 23:42
 */
@Api(description = "sinaApi")
public interface SinaApi {

    @ApiOperation(value = "腾讯接口获取最新数据", notes = "腾讯接口获取最新数据")
    void sharesNewData();

    @ApiOperation(value = "网易接口获取历史数据", notes = "网易接口获取历史数据")
    void sharesHistoryData();

    @ApiOperation(value = "getAllShares", notes = "更新所有的股票的基本信息")
    void updateAllShares();
}
