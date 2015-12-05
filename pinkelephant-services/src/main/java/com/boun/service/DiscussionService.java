package com.boun.service;

import com.boun.data.mongo.model.Discussion;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateDiscussionRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateDiscussionRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetDiscussionResponse;
import com.boun.http.response.ListDiscussionResponse;

public interface DiscussionService {

	public Discussion findById(String discussionId);
	
	public ActionResponse createDiscussion(CreateDiscussionRequest request);
	
	public ActionResponse updateDiscussion(UpdateDiscussionRequest request);
	
	public GetDiscussionResponse queryDiscussion(BasicQueryRequest request);
	
	public ListDiscussionResponse listDiscussions(BasicQueryRequest request);
	
	public ActionResponse tag(TagRequest request);
}
