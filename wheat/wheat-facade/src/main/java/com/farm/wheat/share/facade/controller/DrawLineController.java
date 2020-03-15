package com.farm.wheat.share.facade.controller;

import com.farm.common.utils.ConvertUtil;
import com.farm.wheat.share.api.search.DrawLineApi;
import com.farm.wheat.share.api.vo.DrawLineVO;
import com.farm.wheat.share.biz.dto.DrawLineDTO;
import com.farm.wheat.share.biz.po.EventPO;
import com.farm.wheat.share.biz.service.search.IDrawLineService;
import com.farm.wheat.share.biz.vo.DrawLineData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("draw/line")
public class DrawLineController implements DrawLineApi {
    @Autowired
    private IDrawLineService drawLineService;

    @RequestMapping(value = "/sharePrices", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public DrawLineData sharePrices(@RequestBody DrawLineVO drawLineVO) throws Exception {
        return drawLineService.sharePrices(ConvertUtil.convert(drawLineVO, DrawLineDTO.class));
    }
}
