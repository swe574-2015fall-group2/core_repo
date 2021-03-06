package com.boun.web.controller;

import com.boun.data.mongo.model.User;
import com.boun.http.request.*;
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

import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetUserResponse;
import com.boun.http.response.LoginResponse;
import com.boun.http.response.SearchUserResponse;
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
    public @ResponseBody User createUser(@RequestBody CreateUserRequest request) {

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

	@ApiOperation(value="Update User")
	@RequestMapping(value="update", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public @ResponseBody ActionResponse updateUser(@RequestBody UpdateUserRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("updateUser request received, request->" + request.toString());
			}
			return userService.updateUser(request);
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("updateUser operation finished");
			}
		}
	}

	@ApiOperation(value="Set User Roles")
	@RequestMapping(value="roles", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public @ResponseBody User setUserRoles(@RequestBody SetRolesRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("setUserRoles request received, request->" + request.toString());
			}
			return userService.setRoles(request);
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("setUserRoles operation finished");
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
    
    @ApiOperation(value="Logout")
    @RequestMapping(value="logout", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody ActionResponse logout(@RequestBody BaseRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("logout request received, request->" + request.toString());
    		}
    		return userService.logout(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("logout operation finished, username->" + request.getAuthToken());
    		}
    	}
    }
 
    @ApiOperation(value="Reset Password")
    @RequestMapping(value="resetPassword", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
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

    @ApiOperation(value="Change Password")
    @RequestMapping(value="changePassword", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody ActionResponse changePassword(@RequestBody ChangePasswordRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("changePassword request received, oneTimeToken->" + request.getOneTimeToken());
    		}
    		return userService.changePassword(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("changePassword operation finished, oneTimeToken->" + request.getOneTimeToken());
    		}
    	}
    }
    
    @ApiOperation(value="Upload Image")
    @RequestMapping(value="uploadImage", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody ActionResponse uploadImage(@RequestBody UploadImageRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("uploadImage request received");
    		}
    		return userService.uploadImage(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("uploadImage operation finished");
    		}
    	}
    }
    
    @ApiOperation(value="Get User with ID")
    @RequestMapping(value="get", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody GetUserResponse getUser(@RequestBody BasicQueryRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("getUser request received");
    		}
    		return userService.getUser(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("getUser operation finished");
    		}
    	}
    }
    
    @ApiOperation(value="Query User")
    @RequestMapping(value="query", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody SearchUserResponse searchUser(@RequestBody BasicSearchRequest request){
    	
    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("searchUser request received");
    		}
    		return userService.searchUser(request);	
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("searchUser operation finished");
    		}
    	}
    }
}
