package com.boun.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.request.AuthenticationRequest;
import com.boun.http.request.CreateUserRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;
import com.boun.service.UserService;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value="create", method = RequestMethod.POST)
    public @ResponseBody ActionResponse createUser(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);
    }
    
    
    @RequestMapping(value="login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse authenticate(@RequestBody AuthenticationRequest request){
    	
    	return userService.authenticate(request);
    }
}
