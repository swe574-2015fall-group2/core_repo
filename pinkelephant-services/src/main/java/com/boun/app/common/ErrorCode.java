package com.boun.app.common;

import java.text.MessageFormat;

public enum ErrorCode {

	USER_NOT_FOUND("User not found"),
	DUPLICATE_USER("There is already a user with given username"),
	OPERATION_NOT_ALLOWED("Operation is not allowed for not authenticated users"),

	DUPLICATE_GROUP("There is already a group with given name"),
	GROUP_NOT_FOUND("Group not found"),
	
	MEETING_NOT_FOUND("Meeting not found"),
	INVALID_INPUT("Invalid input-> {0}"),
	
	USER_IS_ALREADY_A_MEMBER("User is already member of this group"),
	USER_IS_BLOCKED("User is blocked for this group"),
	
	DUPLICATE_ROLE("There is already a role with given name"),
	ROLE_NOT_FOUND("Role not found");

	private String message;
	
	private ErrorCode(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String format(Object... args){
		return MessageFormat.format(message, args);
	}
}
