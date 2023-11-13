package com.example.gbatchmongo.common.enums;

public enum SkuType {

    MASTER(0, "Master SKU");

    private final Integer code;
    private final String value;
    private SkuType(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }
}
