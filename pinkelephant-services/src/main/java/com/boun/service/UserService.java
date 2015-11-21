package com.boun.service;

import com.boun.data.mongo.model.User;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;


public interface UserService {

    User findById(String id);

	LoginResponse authenticate(AuthenticationRequest request);
	
    User createUser(CreateUserRequest request);

    ActionResponse updateUser(UpdateUserRequest request);

    ActionResponse resetPassword(ResetPasswordRequest request);
    
    ActionResponse changePassword(ChangePasswordRequest request);

    User setRoles(SetRolesRequest request);
}
