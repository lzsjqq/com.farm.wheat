package com.farm.wheat.share.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-28 23:42
 */
@Api(description = "sinaApi")
public interface SinaApi {

    @ApiOperation(value = "test", notes = "test")
    void test(String code);
}
