package com.farm.wheat.share.facade.controller;

import com.farm.common.utils.ConvertUtil;
import com.farm.wheat.share.api.deal.DealApi;
import com.farm.wheat.share.api.vo.CompleteDealVO;
import com.farm.wheat.share.api.vo.DealDetailInfoVO;
import com.farm.wheat.share.api.vo.DealInfoVO;
import com.farm.wheat.share.api.vo.EventVO;
import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.po.EventPO;
import com.farm.wheat.share.biz.service.deal.DealService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("deal")
public class DealController implements DealApi {

    @Autowired
    private DealService dealService;

    @RequestMapping(value = "/dealInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Override
    public PageInfo<DealInfoVO> dealInfo(@RequestBody DealInfoDTO dealInfoDTO) {
        return ConvertUtil.convert(dealService.dealInfo(dealInfoDTO), PageInfo.class);
    }
    @RequestMapping(value = "/updateDealInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Override
    public void updateDealInfo(@RequestBody DealInfoDTO dealInfoDTO) throws Exception {
        dealService.updateDealInfo(dealInfoDTO);
    }

    @RequestMapping(value = "/dealDetailInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Override
    public PageInfo<DealDetailInfoPO> dealDetailInfo(@RequestBody DealDetailInfoDTO dealDetailInfoDTO) {
        return ConvertUtil.convert(dealService.dealDetailInfo(dealDetailInfoDTO), PageInfo.class);
    }

    @RequestMapping(value = "/insertDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @Override
    public int insertDetail(@RequestBody DealDetailInfoVO dealDetailInfoVO) throws Exception {
        return dealService.insertDetail(ConvertUtil.convert(dealDetailInfoVO, DealDetailInfoDTO.class));
    }

    @Override
    @RequestMapping(value = "/insertEvent", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void insertEvent(EventVO eventVO) throws Exception {
        dealService.insertEvent(ConvertUtil.convert(eventVO, EventPO.class));
    }

    @Override
    @RequestMapping(value = "/completeDeal", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public void completeDeal(CompleteDealVO completeDealVO) throws Exception {
        dealService.completeDeal(completeDealVO.getShareCode());
    }


}
