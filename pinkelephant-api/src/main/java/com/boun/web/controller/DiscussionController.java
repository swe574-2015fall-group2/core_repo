package com.boun.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.request.AddCommentRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateDiscussionRequest;
import com.boun.http.request.LinkRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateDiscussionRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetDiscussionResponse;
import com.boun.http.response.ListDiscussionResponse;
import com.boun.service.CommentService;
import com.boun.service.DiscussionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "discussion", description = "Discussion service")
@RequestMapping("/v1/discussion")
public class DiscussionController {
	
	private final static Logger logger = LoggerFactory.getLogger(DiscussionController.class);

	@Autowired
	private DiscussionService discussionService;

	@Autowired
	private CommentService commentService;
	
	@ApiOperation(value = "Create Discussion")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse createDiscussion(@RequestBody CreateDiscussionRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("createDiscussion request received, request->" + request.toString());
			}
			return discussionService.createDiscussion(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("createDiscussion operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Update Discussion")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse updateDiscussion(@RequestBody UpdateDiscussionRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("updateDiscussion request received, request->" + request.toString());
			}
			return discussionService.updateDiscussion(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("updateDiscussion operation finished");
			}
		}
	}
	
	@ApiOperation(value = "List Discussions of a group")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ListDiscussionResponse listDiscussions(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("listDiscussions request received, request->" + request.toString());
			}
			return discussionService.listDiscussions(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("listDiscussions operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Query a Discussion")
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody GetDiscussionResponse queryDiscussion(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryDiscussion request received, request->" + request.toString());
			}
			return discussionService.queryDiscussion(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryDiscussion operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Add a Comment to a Discussion")
	@RequestMapping(value = "addComment", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse createComment(@RequestBody AddCommentRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("createComment request received, request->" + request.toString());
			}
			return commentService.createComment(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("createComment operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Add/Remove tag to/from a Discussion")
	@RequestMapping(value = "tag", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse tag(@RequestBody TagRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("tag request received, request->" + request.toString());
			}
			return discussionService.tag(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("tag operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Link a meeting to a discussion")
	@RequestMapping(value = "linkMeeting", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse linkMeeting(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("linkMeeting request received, request->" + request.toString());
			}
			return discussionService.linkMeeting(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("linkMeeting operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Link a resource to a discussion")
	@RequestMapping(value = "linkResource", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse linkResource(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("linkResource request received, request->" + request.toString());
			}
			return discussionService.linkResource(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("linkResource operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Removes link between discussion and resource")
	@RequestMapping(value = "removeResourceLink", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse removeResourceLink(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeResourceLink request received, request->" + request.toString());
			}
			return discussionService.removeResourceLink(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("removeResourceLink operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Removes link between discussion and meeting")
	@RequestMapping(value = "removeMeetingLink", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse removeMeetingLink(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeMeetingLink request received, request->" + request.toString());
			}
			return discussionService.removeMeetingLink(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("removeMeetingLink operation finished");
			}
		}
	}
}
