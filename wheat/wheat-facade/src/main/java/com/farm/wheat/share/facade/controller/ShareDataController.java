package com.farm.wheat.share.facade.controller;

import com.farm.wheat.share.api.download.SinaApi;
import com.farm.wheat.share.service.service.processer.AllSharesBySinaProcessor;
import com.farm.wheat.share.service.service.processer.ShareHistoryDataByWYProcessor;
import com.farm.wheat.share.service.service.processer.ShareNewestByTbProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 19:47
 */
@RestController("share")
public class ShareDataController implements SinaApi {

    @Autowired
    private ShareNewestByTbProcessor shareNewestByTbProcessor;
    @Autowired
    private AllSharesBySinaProcessor allSharesBySinaProcessor;

    @Autowired
    private ShareHistoryDataByWYProcessor shareHistoryDataByWYProcessor;


    @RequestMapping(value = "/sharesNewData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public void sharesNewData() {
        shareNewestByTbProcessor.start();
    }


    @RequestMapping(value = "/sharesHistoryData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public void sharesHistoryData() {
        shareHistoryDataByWYProcessor.start();
    }


    @RequestMapping(value = "/updateAllShares", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public void updateAllShares() {
        allSharesBySinaProcessor.start();
    }

}
