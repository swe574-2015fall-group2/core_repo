package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AuthenticationRequest {

	@NotNull
	private String username;
	
	@NotNull
	private String password;
}
