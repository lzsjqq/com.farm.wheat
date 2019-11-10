package com.farm.wheat.share.biz.service.deal.impl;

import com.farm.wheat.share.biz.dto.DealInfoDTO;
import com.farm.wheat.share.biz.dto.ShareInfoDto;
import com.farm.wheat.share.biz.mapper.simple.DealInfoMapper;
import com.farm.wheat.share.biz.po.DealInfoPO;
import com.farm.wheat.share.biz.service.deal.DealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.ws.soap.Addressing;

/**
 * 交易记录相关
 */
@Service
public class DealServiceImpl implements DealService {
    @Resource
    private DealInfoMapper dealInfoMapper;

    @Override
    public PageInfo<DealInfoPO> dealInfo(DealInfoDTO record) {
        PageHelper.startPage(record.getPageNum(), record.getPageSize());
        Page<DealInfoPO> shareInfos = dealInfoMapper.selectBySelective(record);
        return  new PageInfo(shareInfos);
    }
}
