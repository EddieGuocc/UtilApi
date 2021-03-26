package com.gcy.bootwithutils.common.constants;

public enum RedisLockTypeEnum {
    BUSINESS_A("A1", "A1 DESC"),
    BUSINESS_B("B1", "B1 DESC");

    private final String code;
    private final String desc;

    RedisLockTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getUniqueKey(String key) {
        return String.format("%s%s", this.getCode(), key);
    }
}
