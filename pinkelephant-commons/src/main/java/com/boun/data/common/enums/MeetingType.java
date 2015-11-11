package com.boun.data.common.enums;

public enum MeetingType {

    FACE_TO_FACE(1),
    ONLINE(2);

    private int value;

    private MeetingType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
