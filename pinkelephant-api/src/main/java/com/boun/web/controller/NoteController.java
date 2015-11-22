package com.boun.web.controller;

import com.boun.data.mongo.model.Note;
import com.boun.http.request.*;
import com.boun.service.NoteService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "note", description = "Note service")
@RequestMapping("/v1/note")
public class NoteController {

	private final static Logger logger = LoggerFactory.getLogger(NoteController.class);
	
    @Autowired
    private NoteService noteService;

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
}