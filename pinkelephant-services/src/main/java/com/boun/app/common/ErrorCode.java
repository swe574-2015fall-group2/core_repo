package com.boun.app.common;

import java.text.MessageFormat;

public enum ErrorCode {

	USER_NOT_FOUND("User not found", "100"),
	DUPLICATE_USER("There is already a user with given username", "101"),
	OPERATION_NOT_ALLOWED("Operation is not allowed for not authenticated users", "102"),

	DUPLICATE_GROUP("There is already a group with given name", "103"),
	GROUP_NOT_FOUND("Group not found", "104"),
	
	MEETING_NOT_FOUND("Meeting not found", "105"),
	INVALID_INPUT("Invalid input-> {0}", "106"),
	
	USER_IS_ALREADY_A_MEMBER("User is already member of this group", "107"),
	USER_IS_NOT_A_MEMBER("User is not a  member of this group", "108"),
	USER_IS_BLOCKED("User is blocked for this group", "109"),
	
	DUPLICATE_ROLE("There is already a role with given name", "110"),
	ROLE_NOT_FOUND("Role not found", "111"),
	SOME_ROLES_NOT_FOUND("Some roles could not be found", "112"),
	
	INTERNAL_SERVER_ERROR("Internal Server Error", "900")
	;

	private String message;
	private String code;
	
	private ErrorCode(String message, String errorCode) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getCode() {
		return code;
	}
	
	public String format(Object... args){
		return MessageFormat.format(message, args);
	}
}
