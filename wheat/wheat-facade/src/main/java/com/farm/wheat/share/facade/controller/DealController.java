package com.farm.wheat.share.facade.controller;

import com.farm.common.utils.ConvertUtil;
import com.farm.wheat.share.api.deal.DealApi;
import com.farm.wheat.share.api.vo.DealInfoVO;
import com.farm.wheat.share.api.vo.ShareInfoVO;
import com.farm.wheat.share.api.vo.request.SharesReq;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.farm.wheat.share.biz.service.deal.DealService;
import com.farm.wheat.share.biz.service.processer.ShareNewestByTbProcessor;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("deal")
public class DealController implements DealApi {

    @Autowired
    private DealService dealService;

    @RequestMapping(value = "/info", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Override
    public PageInfo<DealInfoVO> dealInfo(@RequestBody DealInfoDTO dealInfoDTO) {
        return ConvertUtil.convert(dealService.dealInfo(dealInfoDTO), PageInfo.class);
    }

}