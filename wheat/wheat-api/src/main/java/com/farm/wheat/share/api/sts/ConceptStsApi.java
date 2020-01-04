package com.farm.wheat.share.api.sts;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @description:
 * @author: xyc
 * @create: 2019-10-27 23:11
 */
@Api(description = "概念统计")
public interface ConceptStsApi {
    @ApiOperation(value = "概念统计前type数量", notes = "概念统计前type数量")
    void stsTop(String data);
}
