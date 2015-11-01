package com.boun.web.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserService userService;

    @RequestMapping(value="create", method = RequestMethod.POST)
    public @ResponseBody ActionResponse createUser(@RequestBody CreateUserRequest request) {

    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("createUser request received, request->" + request.toString());
    		}
    		return userService.createUser(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("createUser operation finished");
    		}
    	}
    }
    
    @RequestMapping(value="login", method = RequestMethod.POST)
    public @ResponseBody LoginResponse authenticate(@RequestBody AuthenticationRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("login request received, request->" + request.toString());
    		}
    		return userService.authenticate(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("login operation finished, username->" + request.getUsername());
    		}
    	}
    	
    	
    }
}
