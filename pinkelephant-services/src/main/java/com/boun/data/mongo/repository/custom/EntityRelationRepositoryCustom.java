package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.EntityRelation.RelationType;

public interface EntityRelationRepositoryCustom {

	EntityRelation findRelation(String fromEntityId, RelationType fromType, String toEntityId, RelationType toType);
	
	List<EntityRelation> findRelationByMeetingId(String meetingId);
	
	List<EntityRelation> findRelationByDiscussionId(String discussionId);
	
	List<EntityRelation> findRelationByResourceId(String resourceId);
}
