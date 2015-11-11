package com.boun.data.common.enums;

public enum MeetingInvitationResult {

    ACCEPT(1),
    REJECT(2),
    TENTATIVE(3)
    
    ;

    private int value;

    private MeetingInvitationResult(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
