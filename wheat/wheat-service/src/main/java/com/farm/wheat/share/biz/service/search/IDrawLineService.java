package com.farm.wheat.share.biz.service.search;

import com.farm.wheat.share.biz.dto.DrawLineDTO;
import com.farm.wheat.share.biz.vo.DrawLineData;

import java.util.List;

/**
 * 画线相关
 */
public interface IDrawLineService {

    DrawLineData sharePrices(DrawLineDTO drawLine) throws Exception;
}
