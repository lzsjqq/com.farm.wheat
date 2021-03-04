package com.farm.wheat.share.service.service.search;

import com.farm.wheat.share.service.dto.ShareInfoDto;
import com.github.pagehelper.PageInfo;

/**
 * @description: 股票信息查询
 * @author: xyc
 * @create: 2019-09-07 14:37
 */
public interface ISharesSearchService {

    PageInfo<ShareInfoDto> getShareInfoByPage(ShareInfoDto record);
}
