package com.farm.wheat.share.facade.controller;

import com.farm.common.utils.ConvertUtil;
import com.farm.wheat.share.api.search.SimpleApi;
import com.farm.wheat.share.api.vo.ShareInfoVO;
import com.farm.wheat.share.api.vo.request.SharesReq;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.service.search.ISharesSearchService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description:
 * @author: xyc
 * @create: 2019-09-07 14:35
 */
@RestController
@RequestMapping("simple")
public class SimpleController implements SimpleApi {

    @Autowired
    private ISharesSearchService sharesSearchService;

    @RequestMapping(value = "/shares", method = RequestMethod.POST,produces = "application/json; charset=utf-8")
    @Override
    @ResponseBody
    public PageInfo<ShareInfoVO> getShares(@RequestBody SharesReq sharesReq) {
        PageInfo<ShareInfoDto> shareInfos = sharesSearchService.getShareInfoByPage(ConvertUtil.convert(sharesReq, ShareInfoDto.class));
        return ConvertUtil.convert(shareInfos, PageInfo.class);
    }
}
