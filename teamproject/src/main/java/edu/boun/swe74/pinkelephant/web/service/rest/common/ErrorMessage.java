package edu.boun.swe74.pinkelephant.web.service.rest.common;

public enum ErrorMessage {

	AUTHENTICATION_FAILED("Authentication is failed for given user"),
	
	UNEXPECTED_ERROR("Unexpected error occured");
	
	private ErrorMessage(String value) {
		this.value = value;
	}
	
	private String value;

	public String getValue() {
		return value;
	}
}
