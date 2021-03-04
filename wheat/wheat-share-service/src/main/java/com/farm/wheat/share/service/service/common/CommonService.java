package com.farm.wheat.share.service.service.common;

import com.farm.wheat.share.service.po.SysDicPO;

public interface CommonService {
    /**
     * 查询一个字典
     *
     * @param dicCode
     * @return
     */
    SysDicPO dicOne(String dicCode);
}
