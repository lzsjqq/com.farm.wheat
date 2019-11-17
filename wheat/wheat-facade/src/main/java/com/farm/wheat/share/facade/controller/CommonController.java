package com.farm.wheat.share.facade.controller;

import com.farm.wheat.share.api.deal.CommonApi;
import com.farm.wheat.share.biz.po.SysDicPO;
import com.farm.wheat.share.biz.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("common")
public class CommonController implements CommonApi {

    @Autowired
    private CommonService commonService;

    @RequestMapping(value = "/dicOne", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Override
    public SysDicPO dicOne(@RequestParam String dicCode) {
        return commonService.dicOne(dicCode);

    }
}
