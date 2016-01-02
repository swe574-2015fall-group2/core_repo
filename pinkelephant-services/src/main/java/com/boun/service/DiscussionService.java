package com.boun.service;

import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetDiscussionResponse;
import com.boun.http.response.ListDiscussionResponse;

public interface DiscussionService {

	Discussion findById(String discussionId);

	boolean archiveDiscussion(BasicDeleteRequest request);

	ActionResponse createDiscussion(CreateDiscussionRequest request);
	
	ActionResponse updateDiscussion(UpdateDiscussionRequest request);
	
	GetDiscussionResponse queryDiscussion(BasicQueryRequest request);
	
	ListDiscussionResponse listDiscussions(BasicQueryRequest request);
	
	ActionResponse tag(TagRequest request);
	
	ActionResponse link(LinkRequest request, EntityType toType);
	
	ActionResponse removeLink(LinkRequest request, EntityType toType);
}
