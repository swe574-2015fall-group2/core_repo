package com.boun.web.controller;

import com.boun.data.mongo.model.Note;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.ListNoteResponse;
import com.boun.http.response.NoteResponse;
import com.boun.service.NoteService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "note", description = "Note service")
@RequestMapping("/v1/note")
public class NoteController {

	private final static Logger logger = LoggerFactory.getLogger(NoteController.class);
	
    @Autowired
    private NoteService noteService;

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public String handleException1(HttpMessageNotReadableException ex)
	{
		ex.printStackTrace();
	    return ex.getMessage();
	}
	
	@ApiOperation(value="Create Note")
	@RequestMapping(value="create", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public @ResponseBody
	Note createNote(@RequestBody CreateNoteRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("createNote request received, request->" + request.toString());
			}
			return noteService.createNote(request);
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("createNote operation finished");
			}
		}
	}

	@ApiOperation(value="Update Note")
	@RequestMapping(value="update", method = RequestMethod.POST)
	@ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
	public @ResponseBody
	Note updateNote(@RequestBody UpdateNoteRequest request) {

		try{
			if(logger.isDebugEnabled()){
				logger.debug("updateNote request received, request->" + request.toString());
			}
			return noteService.updateNote(request);
		}finally{
			if(logger.isDebugEnabled()){
				logger.debug("updateNote operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Add/Remove tag to/from a Note")
	@RequestMapping(value = "tag", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody ActionResponse tag(@RequestBody TagRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("tag request received, request->" + request.toString());
			}
			return noteService.tag(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("tag operation finished");
			}
		}
	}

	@ApiOperation(value = "Query Notes by group ID")
	@RequestMapping(value = "queryByGroup", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	ListNoteResponse queryNotesByGroupId(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryNoteByGroupId request received, request->" + request.toString());
			}
			return noteService.queryNotesOfGroup(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryNoteByGroupId operation finished");
			}
		}
	}

	@ApiOperation(value = "Query Note")
	@RequestMapping(value = "query", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	NoteResponse queryNote(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryNote request received, request->" + request.toString());
			}
			return noteService.queryNote(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryNote operation finished");
			}
		}
	}
	
	@ApiOperation(value = "Query Notes of a Meeting")
	@RequestMapping(value = "queryByMeeting", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	public @ResponseBody
	ListNoteResponse queryNoteByMeeting(@RequestBody BasicQueryRequest request) {

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("queryNoteByMeeting request received, request->" + request.toString());
			}
			return noteService.queryNotesOfMeeting(request);
		} finally {
			if (logger.isDebugEnabled()) {
				logger.debug("queryNoteByMeeting operation finished");
			}
		}
	}
}
