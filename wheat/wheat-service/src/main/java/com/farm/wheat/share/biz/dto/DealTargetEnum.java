package com.farm.wheat.share.biz.dto;

/**
 * \1=买入 2=卖出
 */
public enum DealTargetEnum {

    /**
     * 买入
     */
    MR("1"),
    /**
     * 卖出
     */
    MC("2");
    private String value;

    DealTargetEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
