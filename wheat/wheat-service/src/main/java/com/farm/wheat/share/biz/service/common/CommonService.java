package com.farm.wheat.share.biz.service.common;

import com.farm.wheat.share.biz.po.SysDicPO;

public interface CommonService {
    /**
     * 查询一个字典
     *
     * @param dicCode
     * @return
     */
    SysDicPO dicOne(String dicCode);
}
