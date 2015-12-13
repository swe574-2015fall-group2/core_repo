package com.boun.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.data.mongo.model.Role;
import com.boun.http.request.CreateRoleRequest;
import com.boun.http.request.UpdateRoleRequest;
import com.boun.service.RoleService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "role", description = "Role service")
@RequestMapping("/v1/role")
public class RoleController {

	private final static Logger logger = LoggerFactory.getLogger(RoleController.class);
	
    @Autowired
    private RoleService roleService;

    @ApiOperation(value="Create Role")
    @RequestMapping(value="create", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody Role createRole(@RequestBody CreateRoleRequest request) {

    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("createRole request received, request->" + request.toString());
    		}
    		return roleService.createRole(request);
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("createRole operation finished");
    		}
    	}
    }

	@ApiOperation(value="Update Role")
	@RequestMapping(value="update", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public @ResponseBody Role updateRole(@RequestBody UpdateRoleRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("updateRole request received, request->" + request.toString());
			}
			return roleService.updateRole(request);
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("updateRole operation finished");
			}
		}
	}

	@ApiOperation(value = "Lists all roles")
	@RequestMapping(value = "/roles/all", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public List<Role> getRolesAll() {
		// TODO General response template should be applied
		List<Role> roles = roleService.findAll();

		return roles;
	}

	@ApiOperation(value = "Delete a user role")
	@RequestMapping(value="/role/{id}", method = RequestMethod.DELETE)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public boolean deleteRole(@PathVariable String id) {
		//TODO add userId to log
		return roleService.delete(id);
	}
}
