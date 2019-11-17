package com.farm.wheat.share.facade.controller;

import com.farm.common.utils.ConvertUtil;
import com.farm.wheat.share.api.deal.DealApi;
import com.farm.wheat.share.api.vo.CompleteDealVO;
import com.farm.wheat.share.api.vo.DealDetailInfoVO;
import com.farm.wheat.share.api.vo.DealInfoVO;
import com.farm.wheat.share.biz.dto.DealDetailInfoDTO;
import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.po.DealDetailInfoPO;
import com.farm.wheat.share.biz.service.deal.DealService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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

    @RequestMapping(value = "/dealDetailInfo", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Override
    public PageInfo<DealDetailInfoPO> dealDetailInfo(@RequestBody DealDetailInfoDTO dealDetailInfoDTO) {
        return ConvertUtil.convert(dealService.dealDetailInfo(dealDetailInfoDTO), PageInfo.class);
    }

    @RequestMapping(value = "/insertDetail", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @Override
    public int insertDetail(DealDetailInfoVO dealDetailInfoVO) throws Exception {
        return dealService.insertDetail(ConvertUtil.convert(dealDetailInfoVO, DealDetailInfoDTO.class));
    }

    @Transactional
    @Override
    public void completeDeal(CompleteDealVO completeDealVO) throws Exception {
        dealService.completeDeal(completeDealVO.getShareCode());
    }


}
