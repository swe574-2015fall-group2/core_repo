package com.boun.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.QueryMeetingRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.service.MeetingService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "meeting", description = "Meeting service")
@RequestMapping("/v1/meeting")
public class MeetingController {
	
	private final static Logger logger = LoggerFactory.getLogger(MeetingController.class);

    @Autowired
    private MeetingService meetingService;
    
	@ApiOperation(value = "Create Meeting")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody CreateResponse createMeeting(@RequestBody CreateMeetingRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("createMeeting request received, request->" + request.toString());
			}
			return meetingService.createMeeting(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("createMeeting operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Update Meeting")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse updateMeeting(@RequestBody UpdateMeetingRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("updateMeeting request received, request->" + request.toString());
			}
			return meetingService.updateMeeting(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("updateMeeting operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Invite User")
	@RequestMapping(value = "inviteUser", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse inviteUser(@RequestBody InviteUserToMeetingRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("inviteUser request received, request->" + request.toString());
			}
			return meetingService.inviteUser(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("inviteUser operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Query Meetings by group ID")
	@RequestMapping(value = "queryMeetingByGroup", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse queryMeetingByGroupId(@RequestBody QueryMeetingRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryMeetingByGroupId request received, request->" + request.toString());
			}
			return meetingService.queryMeetingsOfGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryMeetingByGroupId operation finished");
			}
		}
	}
}
