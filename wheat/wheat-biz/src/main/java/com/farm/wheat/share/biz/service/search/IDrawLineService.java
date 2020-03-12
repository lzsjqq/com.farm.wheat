package com.farm.wheat.share.biz.service.search;

import com.farm.wheat.share.biz.dto.DrawLineDTO;

import java.util.List;

/**
 * 画线相关
 */
public interface IDrawLineService {

    List<Object[]> sharePrices(DrawLineDTO drawLine) throws Exception;
}
