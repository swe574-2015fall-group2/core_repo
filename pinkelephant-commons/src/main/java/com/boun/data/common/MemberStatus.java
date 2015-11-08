package com.boun.data.common;

public enum MemberStatus {

    ACTIVE(1),
    BLOCKED(2);

    private int value;

    private MemberStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
