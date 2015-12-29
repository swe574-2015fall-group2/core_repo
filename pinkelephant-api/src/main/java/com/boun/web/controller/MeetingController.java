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
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.LinkRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.MeetingProposalInvitationReplyRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateMeetingProposalRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingProposalRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetMeetingResponse;
import com.boun.http.response.ListMeetingResponse;
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
	@RequestMapping(value = "queryByGroup", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListMeetingResponse queryMeetingByGroupId(@RequestBody BasicQueryRequest request) {

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
	@RequestMapping(value = "createProposal", method = RequestMethod.POST)
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
	
	@ApiOperation(value = "Update Meeting Proposal")
	@RequestMapping(value = "updateProposal", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse updateMeetingProposal(@RequestBody UpdateMeetingProposalRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("updateMeetingProposal request received, request->" + request.toString());
			}
			return meetingProposalService.updateMeetingProposal(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("updateMeetingProposal operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Delete Meeting Proposal")
	@RequestMapping(value = "deleteProposal", method = RequestMethod.POST)
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
	@RequestMapping(value = "getProposal", method = RequestMethod.POST)
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
	@RequestMapping(value = "replyProposalInvitation", method = RequestMethod.POST)
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
	
	@ApiOperation(value = "Query Meeting with ID")
	@RequestMapping(value = "get", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody GetMeetingResponse getMeeting(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getMeeting request received, request->" + request.toString());
			}
			return meetingService.getMeeting(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getMeeting operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Add/Remove tag to/from a Meeting")
	@RequestMapping(value = "tag", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse tag(@RequestBody TagRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("tag request received, request->" + request.toString());
			}
			return meetingService.tag(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("tag operation finished");
			}
		}
	}
	
	@ApiOperation(value = "List Meetings of Given User")
	@RequestMapping(value = "myMeetings", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListMeetingResponse getMyMeetings(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getMyMeetings request received, request->" + request.toString());
			}
			return meetingService.getMyMeetings(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getMyMeetings operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Link a discussion to a meeting")
	@RequestMapping(value = "linkDiscussion", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse linkDiscussion(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("linkDiscussion request received, request->" + request.toString());
			}
			return meetingService.link(request, EntityType.DISCUSSION);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("linkDiscussion operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Link a resource to a meeting")
	@RequestMapping(value = "linkResource", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse linkResource(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("linkResource request received, request->" + request.toString());
			}
			return meetingService.link(request, EntityType.RESOURCE);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("linkResource operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Removes link between meeting and resource")
	@RequestMapping(value = "removeResourceLink", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse removeResourceLink(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeResourceLink request received, request->" + request.toString());
			}
			return meetingService.removeLink(request, EntityType.RESOURCE);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("removeResourceLink operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Removes link between meeting and discussion")
	@RequestMapping(value = "removeDiscussionLink", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse removeDiscussionLink(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeDiscussionLink request received, request->" + request.toString());
			}
			return meetingService.removeLink(request, EntityType.DISCUSSION);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("removeDiscussionLink operation finished");
			}
		}
	}
}
