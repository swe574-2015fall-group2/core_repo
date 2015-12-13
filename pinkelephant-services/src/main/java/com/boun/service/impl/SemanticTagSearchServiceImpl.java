package com.boun.service.impl;

import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.cache.TagCache;
import com.boun.data.mongo.model.Tag;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.http.request.BasicSearchRequest;
import com.boun.http.response.SemanticSearchResponse;
import com.boun.service.PinkElephantService;
import com.boun.service.SemanticTagSearchService;
import com.boun.service.TagService;

@Service
public class SemanticTagSearchServiceImpl extends PinkElephantService implements SemanticTagSearchService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private TagService tagService;
	
	@Override
	public SemanticSearchResponse search(BasicSearchRequest request) {

		validate(request);
		
		SemanticSearchResponse response = new SemanticSearchResponse();
		
		String queryString = request.getQueryString();
		if(queryString == null || "".equalsIgnoreCase(queryString)){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Query string cannot be null", "");
		}
		
		Tag tag = TagCache.getInstance(tagService).getTag(queryString);
		if(tag != null){
			addResultList(response, tag, 1);
		}
		
		StringTokenizer tokens = new StringTokenizer(queryString, " ");
		while (tokens.hasMoreElements()) {
			String token = tokens.nextToken();
			
			tag = TagCache.getInstance(tagService).getTag(token);
			
			addResultList(response, tag, 2);
		}
		return response;
	}
	
	private void addResultList(SemanticSearchResponse response, Tag tag, int priority){
		if(tag == null){
			return;
		}
		
		List<TaggedEntity> taggedEntityList = tag.getReferenceSet();
		if(taggedEntityList == null || taggedEntityList.isEmpty()){
			return;
		}
		
		for (TaggedEntity taggedEntity : taggedEntityList) {
			response.addDetail(taggedEntity.getEntityType(), taggedEntity.getId(), taggedEntity.getDescription(), priority);	
		}
	}
	
}
