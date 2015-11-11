package com.boun.data.common.enums;

public enum GroupStatus {

    ACTIVE(1),
    DELETED(2);

    private int value;

    private GroupStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
