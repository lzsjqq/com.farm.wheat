package com.farm.wheat.share.biz.constant;

/**
 * @description:
 * @author: xyc
 * @create: 2019-08-25 22:37
 */
public enum ShareSource {
    SH("上证", 100),
    SZ("深证主板", 200),
    SZ_CY("深证创业板", 201),
    SZ_ZXB("深证中小板", 202),
    KC("科创板", 300),
    XSB("新三板", 400),
    OTHER("其他", 500);
    private String desc;

    private int source;


    ShareSource(String desc, int source) {
        this.source = source;
        this.desc = desc;
    }

    public static Integer getSource(String desc) {
        ShareSource[] values = ShareSource.values();
        for (ShareSource value : values) {
            if (desc.contains(value.getDesc())) {
                return value.getSource();
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
