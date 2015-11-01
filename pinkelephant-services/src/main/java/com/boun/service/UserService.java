package com.boun.service;

import com.boun.http.request.AuthenticationRequest;
import com.boun.http.request.ChangePasswordRequest;
import com.boun.http.request.CreateUserRequest;
import com.boun.http.request.ResetPasswordRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;


public interface UserService {

	LoginResponse authenticate(AuthenticationRequest request);
	
    ActionResponse createUser(CreateUserRequest request);
    
    ActionResponse resetPassword(ResetPasswordRequest request);
    
    ActionResponse changePassword(ChangePasswordRequest request);
}
