package com.farm.wheat.share.facade.controller;

import com.farm.wheat.share.api.sts.ConceptStsApi;
import com.farm.wheat.share.api.vo.ShareInfoVO;
import com.farm.wheat.share.biz.service.statistics.IConceptSts;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xyc
 * @create: 2019-10-27 23:10
 */
@RestController("sts/concept")
public class ConceptStsController implements ConceptStsApi {

    @Autowired
    private IConceptSts conceptSts;

    @RequestMapping(value = "/top", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public void stsTop(@RequestParam String data) {
        try {
            conceptSts.stsTopRatio(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
