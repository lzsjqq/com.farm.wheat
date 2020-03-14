package com.farm.wheat.share.facade.controller;

import com.farm.wheat.share.api.chan.ChanApi;
import com.farm.wheat.share.biz.service.chan.IChanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 构造缠论结构
 * @author: xyc
 * @create: 2020-03-13 22:03
 */
@RestController
@RequestMapping("chan")
public class ChanController implements ChanApi {

    @Autowired
    private IChanService chanService;

    @RequestMapping(value = "/build", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @Override
    public void build(@RequestParam String shareCode) {
        chanService.build(shareCode);
    }
}