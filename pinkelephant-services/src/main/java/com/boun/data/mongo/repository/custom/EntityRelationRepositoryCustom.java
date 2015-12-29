package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.EntityRelation;

public interface EntityRelationRepositoryCustom {

	List<EntityRelation> findRelationByMeetingId(String meetingId);
	
	List<EntityRelation> findRelationByDiscussionId(String discussionId);
	
	List<EntityRelation> findRelationByResourceId(String resourceId);
}
