package com.boun.web.controller;

import com.boun.http.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetGroupResponse;
import com.boun.http.response.ListGroupResponse;
import com.boun.service.GroupService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "group", description = "Group service")
@RequestMapping("/v1/group")
public class GroupController {

	private final static Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private GroupService groupService;

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public String handleException1(HttpMessageNotReadableException ex)
	{
		ex.printStackTrace();
	    return ex.getMessage();
	}
	
	@ApiOperation(value = "Create Group")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody CreateResponse createGroup(@RequestBody CreateUpdateGroupRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("createGroup request received, request->" + request.toString());
			}
			return groupService.createGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("createGroup operation finished");
			}
		}
	}

	@ApiOperation(value = "Update Group")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse updateGroup(@RequestBody CreateUpdateGroupRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("updateGroup request received, request->" + request.toString());
			}
			return groupService.updateGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("updateGroup operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Join Group")
	@RequestMapping(value = "join", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse joinGroup(@RequestBody JoinLeaveGroupRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("joinGroup request received, request->" + request.toString());
			}
			return groupService.joinGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("joinGroup operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Leave Group")
	@RequestMapping(value = "leave", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse leaveGroup(@RequestBody JoinLeaveGroupRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("leaveGroup request received, request->" + request.toString());
			}
			return groupService.leaveGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("leaveGroup operation finished");
			}
		}
	}
	
	@ApiOperation(value = "List my groups")
	@RequestMapping(value = "listMyGroups", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	ListGroupResponse getMyGroups(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getMyGroups request received, request->" + request.toString());
			}
			return groupService.getMyGroups(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getMyGroups operation finished");
			}
		}
	}

	@ApiOperation(value = "Archive a group")
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public boolean archiveGroup(@RequestBody BasicDeleteRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("archiveGroup request received, request->" + request.toString());
			}
			return groupService.archiveGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("archiveGroup operation finished");
			}
		}

	}

	@ApiOperation(value = "List all groups")
	@RequestMapping(value = "listAll", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListGroupResponse getAllGroups(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllGroups request received, request->" + request.toString());
			}
			return groupService.getAllGroups(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllGroups operation finished");
			}
		}
	}

	@ApiOperation(value = "List latest groups")
	@RequestMapping(value = "listLatest", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListGroupResponse getLatestGroups(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getLatestGroups request received, request->" + request.toString());
			}
			return groupService.getLatestGroups(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getLatestGroups operation finished");
			}
		}
	}

	@ApiOperation(value = "List popular groups")
	@RequestMapping(value = "listPopularGroups", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListGroupResponse getPopularGroups(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getPopularGroups request received, request->" + request.toString());
			}

			return groupService.getPopularGroups(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getPopularGroups operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Upload image to group")
	@RequestMapping(value = "uploadImage", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse uploadImage(@RequestBody UploadImageRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("uploadImage request received, request->" + request.toString());
			}
			return groupService.uploadImage(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("uploadImage operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Query Group")
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody GetGroupResponse queryGroup(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryGroup request received, request->" + request.toString());
			}
			return groupService.queryGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryGroup operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Add/Remove tag to/from a Group")
	@RequestMapping(value = "tag", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse tag(@RequestBody TagRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("tag request received, request->" + request.toString());
			}
			return groupService.tag(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("tag operation finished");
			}
		}
	}
	
	@ApiOperation(value = "List Recommended Groups")
	@RequestMapping(value = "listRecommended", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListGroupResponse listRecommended(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("listRecommended request received, request->" + request.toString());
			}
			 return groupService.findRecommendedGroups(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("listRecommended operation finished");
			}
		}
	}
}
