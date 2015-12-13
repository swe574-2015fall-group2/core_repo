package com.boun.service;

import com.boun.http.request.BasicSearchRequest;
import com.boun.http.response.SemanticSearchResponse;

public interface SemanticTagSearchService {

	public SemanticSearchResponse search(BasicSearchRequest request);
}
