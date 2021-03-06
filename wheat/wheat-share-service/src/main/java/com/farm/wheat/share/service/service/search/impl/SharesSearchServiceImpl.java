package com.farm.wheat.share.service.service.search.impl;

import com.farm.wheat.share.service.dto.ShareInfoDto;
import com.farm.wheat.share.service.mapper.simple.ShareInfoMapper;
import com.farm.wheat.share.service.service.search.ISharesSearchService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: xyc
 * @create: 2019-09-07 14:39
 */
@Service
public class SharesSearchServiceImpl implements ISharesSearchService {

    @Resource
    private ShareInfoMapper shareInfoMapper;

    @Override
    public PageInfo<ShareInfoDto> getShareInfoByPage(ShareInfoDto record) {
        PageHelper.startPage(record.getPageNum(), record.getPageSize());
        Page<ShareInfoDto> shareInfos = shareInfoMapper.selectShareInfoByPage(record);
        return  new PageInfo(shareInfos);
    }
}
