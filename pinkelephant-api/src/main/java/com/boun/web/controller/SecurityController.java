package com.boun.web.controller;

import com.boun.data.mongo.model.Role;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.service.SecurityService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(value = "security", description = "Security service")
@RequestMapping("/v1/security")
public class SecurityController {

	private final static Logger logger = LoggerFactory.getLogger(SecurityController.class);
	
    @Autowired
    private SecurityService securityService;

    @ApiOperation(value="Create Role")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody ActionResponse createRole(@RequestBody CreateRoleRequest request) {

    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("createRole request received, request->" + request.toString());
    		}
    		return securityService.createRole(request);
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("createRole operation finished");
    		}
    	}
    }

	@RequestMapping(value = "/roles/all", method = RequestMethod.POST)
	@ApiOperation(value = "Lists all roles")
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public List<Role> getRolesAll() {

		List<Role> roles = securityService.findAll();

		return roles;
	}
}
