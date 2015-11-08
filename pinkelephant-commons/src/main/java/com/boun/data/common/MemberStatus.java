package com.boun.data.common;

public enum MemberStatus {

    SUSPENDED(0),
    ACTIVE(1),
    PASSIVE(2),
    BLOCKED(3);

    private int value;

    private MemberStatus(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
