package com.example.gbatchmongo.common.enums;


public enum ChannelType {
    UNKNOWN(0, "UNKNOWN", "UNKNOWN"),
    ASSORTMENT(1, "assortment", "ea"),
    HANDMADE(2, "handmade", "fgm"),
    B2B(3, "b2b", "b2b");

    private final Integer code;
    private final String value;
    private final String name;

    ChannelType(Integer code, String value, String name) {
        this.code = code;
        this.value = value;
        this.name = name;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
