package com.boun.app.common;

public enum ErrorCode {

	USER_NOT_FOUND("User not found"),
	DUPLICATE_USER("There is already a user with given username"),
	OPERATION_NOT_ALLOWED("Operation is not allowed for not authenticated users");
	
	private String message;
	
	private ErrorCode(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
