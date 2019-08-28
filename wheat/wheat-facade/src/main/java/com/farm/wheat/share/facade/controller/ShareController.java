package com.farm.wheat.share.facade.controller;

import com.farm.wheat.share.api.SinaApi;
import com.farm.wheat.share.biz.service.processer.ShareNewestByTbProcessor;

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
public class ShareController implements SinaApi {

    @Autowired
    private ShareNewestByTbProcessor shareNewestByTbProcessor;

    @RequestMapping(value = "/test", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void test(@RequestParam String code) {
        shareNewestByTbProcessor.start(code);
    }
}
