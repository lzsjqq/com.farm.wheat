package com.farm.wheat.share.biz.service.common.impl;

import com.farm.wheat.share.biz.mapper.simple.SysDicMapper;
import com.farm.wheat.share.biz.po.SysDicPO;
import com.farm.wheat.share.biz.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
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
