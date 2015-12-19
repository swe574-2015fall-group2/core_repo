package com.boun.http.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ChangePasswordRequest {

	@NotNull
	private String oneTimeToken;
	
	@NotNull
	@Size(min=4)
	private String newPassword;
}
