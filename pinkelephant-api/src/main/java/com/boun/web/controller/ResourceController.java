package com.boun.web.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.boun.app.exception.PinkElephantValidationException;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.request.LinkRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.ResourceResponse;
import com.boun.service.ResourceService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "resource", description = "Resource service")
@RequestMapping("/v1/resource")
public class ResourceController {

	private final static Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
    @Autowired
    private ResourceService resourceService;

	@ApiOperation(value="Create External Resource")
	@RequestMapping(value="create", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public @ResponseBody
	Resource createInternalResource(@RequestBody CreateResourceRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("createInternalResource request received, request->" + request.toString());
			}
			return resourceService.createExternalResource(request);
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("createInternalResource operation finished");
			}
		}
	}

	@ApiOperation(value = "Create Internal Resource")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public Resource uploadInternalResource(@RequestParam("file") MultipartFile file, @RequestParam("groupId")String groupId, @RequestParam("authToken")String authToken) {
		try {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("uploadResource request received, request->" + file.toString());
				}
				return resourceService.uploadResource(file.getBytes(), file.getOriginalFilename(), groupId, authToken);
			} finally {
				if (logger.isDebugEnabled()) {
					logger.debug("uploadResource operation finished");
				}
			}
		} catch (IOException e) {
			throw new PinkElephantValidationException("Uploaded file is corrupt!");
		}
	}

	@ApiOperation(value = "Download Resource")
	@RequestMapping(value = "downloadResource", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	void downloadResource(@RequestParam("resourceId")String resourceId, @RequestParam("authToken")String authToken,  HttpServletResponse response) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("downloadResource request received, request->" + resourceId);
			}

			try {

				ResourceResponse resourceResponse =  resourceService.downloadResource(resourceId);
				
				response.setContentType("application/force-download");
				response.setContentLength((int) resourceResponse.getFile().length());
				response.setHeader("Content-Transfer-Encoding", "binary");
				response.setHeader("Content-Disposition","attachment; filename=\"" + resourceResponse.getFileName() +"\"");

				IOUtils.copy(new FileInputStream(resourceResponse.getFile()), response.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("downloadResource operation finished");
			}
		}
	}

	@ApiOperation(value = "Delete an external resource")
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public boolean deleteResource(@RequestBody BasicDeleteRequest request) {
		//TODO add userId to log

		return resourceService.delete(request);
	}

	@ApiOperation(value = "Query Resources by group ID")
	@RequestMapping(value = "queryResourcesByGroup", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	List<Resource> queryMeetingByGroupId(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryResourceByGroupId request received, request->" + request.toString());
			}
			return resourceService.queryResourcesOfGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryResourceByGroupId operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Add/Remove tag to/from a Resource")
	@RequestMapping(value = "tag", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse tag(@RequestBody TagRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("tag request received, request->" + request.toString());
			}
			return resourceService.tag(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("tag operation finished");
			}
		}
	}
	
	
	
	@ApiOperation(value = "Link a resource to a discussion")
	@RequestMapping(value = "linkDiscussion", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse linkDiscussion(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("linkDiscussion request received, request->" + request.toString());
			}
			return resourceService.link(request, EntityType.DISCUSSION);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("linkDiscussion operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Link a resource to a meeting")
	@RequestMapping(value = "linkMeeting", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse linkMeeting(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("linkMeeting request received, request->" + request.toString());
			}
			return resourceService.link(request, EntityType.MEETING);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("linkMeeting operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Removes link between resource and meeting")
	@RequestMapping(value = "removeMeetingLink", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse removeMeetingLink(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeMeetingLink request received, request->" + request.toString());
			}
			return resourceService.removeLink(request, EntityType.MEETING);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("removeMeetingLink operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Removes link between resource and discussion")
	@RequestMapping(value = "removeDiscussionLink", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse removeDiscussionLink(@RequestBody LinkRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("removeDiscussionLink request received, request->" + request.toString());
			}
			return resourceService.removeLink(request, EntityType.DISCUSSION);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("removeDiscussionLink operation finished");
			}
		}
	}
}
