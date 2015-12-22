package com.boun.service;

import com.boun.data.mongo.model.Discussion;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateDiscussionRequest;
import com.boun.http.request.LinkRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateDiscussionRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetDiscussionResponse;
import com.boun.http.response.ListDiscussionResponse;

public interface DiscussionService {

	Discussion findById(String discussionId);
	
	ActionResponse createDiscussion(CreateDiscussionRequest request);
	
	ActionResponse updateDiscussion(UpdateDiscussionRequest request);
	
	GetDiscussionResponse queryDiscussion(BasicQueryRequest request);
	
	ListDiscussionResponse listDiscussions(BasicQueryRequest request);
	
	ActionResponse tag(TagRequest request);
	
	ActionResponse linkMeeting(LinkRequest request);
	
	ActionResponse linkResource(LinkRequest request);
	
	ActionResponse removeResourceLink(LinkRequest request);
	
	ActionResponse removeMeetingLink(LinkRequest request);
}
