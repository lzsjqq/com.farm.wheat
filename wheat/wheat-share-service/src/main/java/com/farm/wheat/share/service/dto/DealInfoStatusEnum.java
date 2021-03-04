package com.farm.wheat.share.service.dto;

public enum DealInfoStatusEnum {

    /**
     * 持仓
     */
    CC(1),
    /**
     * 完成
     */
    WC(2);
    private Integer value;

    DealInfoStatusEnum(Integer value){
        this.value=value;
    }

    public Integer getValue() {
        return value;
    }
}
