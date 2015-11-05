package com.boun.service;

import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;


public interface UserService {

	LoginResponse authenticate(AuthenticationRequest request);
	
    ActionResponse createUser(CreateUserRequest request);
    
    ActionResponse resetPassword(ResetPasswordRequest request);
    
    ActionResponse changePassword(ChangePasswordRequest request);

    ActionResponse setRoles(SetRolesRequest request);
}
