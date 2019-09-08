package com.farm.wheat.share.facade.controller;

import com.farm.common.utils.ConvertUtil;
import com.farm.wheat.share.api.search.SimpleApi;
import com.farm.wheat.share.api.vo.ShareInfoVO;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.service.search.ISharesSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value = "/shares", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    @ResponseBody
    public List<ShareInfoVO> getShares() {
        List<ShareInfoDto> shareInfos = sharesSearchService.getShareInfoByPage(null);
        return ConvertUtil.convert(shareInfos, ShareInfoVO.class);
    }
}
