package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ResetPasswordRequest {
	
	@NotNull
	private String username;
}
