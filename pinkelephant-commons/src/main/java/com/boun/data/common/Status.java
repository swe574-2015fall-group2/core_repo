package com.boun.data.common;

public enum Status {

    SUSPENDED(0),
    ACTIVE(1),
    BLOCKED(2),
    NEW(3);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
