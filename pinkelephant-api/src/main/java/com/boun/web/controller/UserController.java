package com.boun.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.request.AuthenticationRequest;
import com.boun.http.request.ChangePasswordRequest;
import com.boun.http.request.CreateUserRequest;
import com.boun.http.request.ResetPasswordRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;
import com.boun.service.UserService;

@RestController
@Api(value = "user", description = "User service")
@RequestMapping("/v1/user")
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserService userService;

    @ApiOperation(value="Create User")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
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

    @ApiOperation(value="Login")
    @RequestMapping(value="login", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
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
    
    @RequestMapping(value="resetPassword", method = RequestMethod.POST)
    public @ResponseBody ActionResponse resetPassword(@RequestBody ResetPasswordRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("resetPassword request received, request->" + request.toString());
    		}
    		return userService.resetPassword(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("resetPassword operation finished, username->" + request.getUsername());
    		}
    	}
    }
    
    @RequestMapping(value="changePassword", method = RequestMethod.POST)
    public @ResponseBody ActionResponse changePassword(@RequestBody ChangePasswordRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("resetPassword request received, oneTimeToken->" + request.getOneTimeToken());
    		}
    		return userService.changePassword(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("resetPassword operation finished, oneTimeToken->" + request.getOneTimeToken());
    		}
    	}
    }
}
