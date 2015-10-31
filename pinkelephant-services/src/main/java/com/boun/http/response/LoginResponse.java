package com.boun.http.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LoginResponse extends ActionResponse{

	private String token;
	
}
