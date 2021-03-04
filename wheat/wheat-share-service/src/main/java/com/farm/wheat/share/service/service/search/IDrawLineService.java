package com.farm.wheat.share.service.service.search;

import com.farm.wheat.share.service.dto.DrawLineDTO;
import com.farm.wheat.share.service.vo.DrawLineData;

/**
 * 画线相关
 */
public interface IDrawLineService {

    DrawLineData sharePrices(DrawLineDTO drawLine) throws Exception;
}
