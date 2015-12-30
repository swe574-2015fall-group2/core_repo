package com.boun.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boun.http.request.BasicSearchRequest;
import com.boun.http.request.TagSearchRequest;
import com.boun.http.response.QueryLabelResponse;
import com.boun.http.response.SemanticSearchResponse;
import com.boun.service.SemanticTagSearchService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@RestController
@Api(value = "search", description = "Semantic Search Service")
@RequestMapping("/v1/semantic")
public class SearchController {

	private final static Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	SemanticTagSearchService semanticSearchService;
	
    @ApiOperation(value="Semantic Search")
    @RequestMapping(value="search", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody SemanticSearchResponse search(@RequestBody TagSearchRequest request) {

    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("search request received, request->" + request.toString());
    		}
    		return semanticSearchService.searchNew(request);
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("search operation finished");
    		}
    	}
    }
    
    @ApiOperation(value="Query Label")
    @RequestMapping(value="queryLabel", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody QueryLabelResponse queryLabel(@RequestBody BasicSearchRequest request) {

    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("queryLabel request received, request->" + request.toString());
    		}
    		return semanticSearchService.queryLabel(request);
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("queryLabel operation finished");
    		}
    	}
    }
    
    @ApiOperation(value="Query Search String")
    @RequestMapping(value="querySearchString", method = RequestMethod.POST)
    @ApiResponses(value={@ApiResponse(code=200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    public @ResponseBody QueryLabelResponse querySearchString(@RequestBody BasicSearchRequest request) {

    	try{
    		if(logger.isDebugEnabled()){
    			logger.debug("querySearchString request received, request->" + request.toString());
    		}
    		return semanticSearchService.querySearchString(request);
    	}finally{
    		if(logger.isDebugEnabled()){
    			logger.debug("querySearchString operation finished");
    		}
    	}
    }
}
