package com.farm.wheat.share.biz.facet.controller;

import com.farm.wheat.share.biz.service.processer.ShareNewestByTbProcessor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 19:47
 */
@RestController
@Api(description = "share")
public class ShareController {

    @Autowired
    private ShareNewestByTbProcessor shareNewestByTbProcessor;

    @ApiOperation(value = "test", notes = "test")
    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void test(@RequestParam String code) {
        shareNewestByTbProcessor.start(code);
    }
}
