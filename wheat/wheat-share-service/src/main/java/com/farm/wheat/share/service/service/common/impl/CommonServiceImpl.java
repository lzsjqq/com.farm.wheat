package com.farm.wheat.share.service.service.common.impl;

import com.farm.wheat.share.service.mapper.simple.SysDicMapper;
import com.farm.wheat.share.service.po.SysDicPO;
import com.farm.wheat.share.service.service.common.CommonService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommonServiceImpl implements CommonService {

    @Resource
    private SysDicMapper sysDicMapper;

    @Override
    public SysDicPO dicOne(String dicCode) {
        return sysDicMapper.dicOne(dicCode);
    }
}
