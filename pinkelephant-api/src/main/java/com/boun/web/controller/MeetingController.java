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
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.MeetingProposalInvitationReplyRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingProposalRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.service.MeetingProposalService;
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
    
    @Autowired
    private MeetingProposalService meetingProposalService;
    
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
	
	@ApiOperation(value = "Reply Invitation")
	@RequestMapping(value = "replyInvitation", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse invitationReply(@RequestBody MeetingInvitationReplyRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("invitationReply request received, request->" + request.toString());
			}
			return meetingService.invitationReply(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("invitationReply operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Query Meetings by group ID")
	@RequestMapping(value = "queryMeetingByGroup", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse queryMeetingByGroupId(@RequestBody BasicQueryRequest request) {

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
	
	@ApiOperation(value = "Create Meeting Proposal")
	@RequestMapping(value = "createMeetingProposal", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse createMeetingProposal(@RequestBody CreateMeetingProposalRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("createMeetingProposal request received, request->" + request.toString());
			}
			return meetingProposalService.createMeetingProposal(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("createMeetingProposal operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Delete Meeting Proposal")
	@RequestMapping(value = "deleteMeetingProposal", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse deleteMeetingProposal(@RequestBody BasicDeleteRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("deleteMeetingProposal request received, request->" + request.toString());
			}
			return meetingProposalService.deleteMeetingProposal(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("deleteMeetingProposal operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Query Meeting Proposal")
	@RequestMapping(value = "getMeetingProposal", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse getMeetingProposal(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getMeetingProposal request received, request->" + request.toString());
			}
			return meetingProposalService.getMeetingProposal(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getMeetingProposal operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Reply to Meeting Proposal Invitation")
	@RequestMapping(value = "replyMeetingProposalInvitation", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse replyInvitation(@RequestBody MeetingProposalInvitationReplyRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("replyInvitation request received, request->" + request.toString());
			}
			return meetingProposalService.replyInvitation(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("replyInvitation operation finished");
			}
		}
	}
}
