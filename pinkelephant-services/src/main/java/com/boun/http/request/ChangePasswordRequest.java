package com.boun.http.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {

	private String oneTimeToken;
	private String newPassword;
}
