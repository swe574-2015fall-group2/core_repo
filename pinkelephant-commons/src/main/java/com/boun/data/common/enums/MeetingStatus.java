package com.boun.data.common.enums;

public enum MeetingStatus {

	NOT_STARTED(1),
	ACTIVE(1), 
	FINISHED(2);

	private int value;

	private MeetingStatus(int value) {
	        this.value = value;
	    }

	public int value() {
		return value;
	}
}
