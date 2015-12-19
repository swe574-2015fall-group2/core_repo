package com.boun.service;

import com.boun.http.request.BasicSearchRequest;
import com.boun.http.request.TagSearchRequest;
import com.boun.http.response.QueryLabelResponse;
import com.boun.http.response.SemanticSearchResponse;

public interface SemanticTagSearchService {

	public SemanticSearchResponse search(TagSearchRequest request);
	
	public QueryLabelResponse queryLabel(BasicSearchRequest request);
}
