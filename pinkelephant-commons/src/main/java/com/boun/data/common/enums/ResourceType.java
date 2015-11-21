package com.boun.data.common.enums;

public enum ResourceType {

    INTERNAL("internal"),
    EXTERNAL("external");

    private String value;

    private ResourceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
