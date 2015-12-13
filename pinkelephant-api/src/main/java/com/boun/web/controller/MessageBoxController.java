package com.boun.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.request.BaseRequest;
import com.boun.http.request.MessageReadRequest;
import com.boun.http.request.SendMessageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetMessageResponse;
import com.boun.service.MessageboxService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "messagebox", description = "Messaging service")
@RequestMapping("/v1/messagebox")
public class MessageBoxController {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageBoxController.class);

    @Autowired
    private MessageboxService messageboxService;
    
	@ApiOperation(value = "Get all messages by receiver")
	@RequestMapping(value = "getByReceiver", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody GetMessageResponse getAllMessagesByReceiver(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllMessagesByReceiver request received, request->" + request.toString());
			}
			return messageboxService.getAllMessagesByReceiver(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllMessagesByReceiver operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Get all messages by sender")
	@RequestMapping(value = "getBySender", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody GetMessageResponse getAllMessagesBySender(@RequestBody BaseRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllMessagesBySender request received, request->" + request.toString());
			}
			return messageboxService.getAllMessagesBySender(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("getAllMessagesBySender operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Send Message")
	@RequestMapping(value = "send", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse sendMessage(@RequestBody SendMessageRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("sendMessage request received, request->" + request.toString());
			}
			return messageboxService.sendMessage(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("sendMessage operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Read Message")
	@RequestMapping(value = "read", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse read(@RequestBody MessageReadRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("read request received, request->" + request.toString());
			}
			return messageboxService.read(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("read operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Unread Message")
	@RequestMapping(value = "unread", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse unread(@RequestBody MessageReadRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("unread request received, request->" + request.toString());
			}
			return messageboxService.unread(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("unread operation finished");
			}
		}
	}
}
