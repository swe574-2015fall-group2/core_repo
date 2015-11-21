package com.boun.web.controller;

import com.boun.app.exception.PinkElephantValidationException;
import com.boun.http.response.CreateResourceResponse;
import com.boun.service.ResourceService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@Api(value = "resource", description = "Resource service")
@RequestMapping("/v1/resource")
public class ResourceController {

	private final static Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
    @Autowired
    private ResourceService resourceService;

	@ApiOperation(value = "Upload resource")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public CreateResourceResponse uploadResource(@RequestParam("file") MultipartFile file) {
		try {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("uploadResource request received, request->" + file.toString());
				}
				return resourceService.uploadResource(file.getBytes(), file.getOriginalFilename());
			} finally {
				if (logger.isDebugEnabled()) {
					logger.debug("uploadResource operation finished");
				}
			}
		} catch (IOException e) {
			throw new PinkElephantValidationException("Uploaded file is corrupt!");
		}
	}
}
