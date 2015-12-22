package com.boun.data.mongo.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.EntityRelation.RelationType;
import com.boun.data.mongo.repository.custom.EntityRelationRepositoryCustom;

public class EntityRelationRepositoryImpl implements EntityRelationRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public EntityRelation findRelation(String fromEntityId, RelationType fromType, String toEntityId, RelationType toType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("entityFrom.$id").is(new ObjectId(fromEntityId)).and("fromType").is(fromType).and("entityTo.$id").is(new ObjectId(toEntityId)).and("toType").is(toType));

		EntityRelation meetingDiscussion = mongoTemplate.findOne(query, EntityRelation.class);

		return meetingDiscussion;
	}

	@Override
	public List<EntityRelation> findRelationByMeetingId(String meetingId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("entityFrom.$id").is(new ObjectId(meetingId)).and("fromType").is(RelationType.MEETING));

		List<EntityRelation> meetingDiscussionList = mongoTemplate.find(query, EntityRelation.class);

		return meetingDiscussionList;
	}
	
	@Override
	public List<EntityRelation> findRelationByDiscussionId(String discussionId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("entityFrom.$id").is(new ObjectId(discussionId)).and("fromType").is(RelationType.DISCUSSION));

		List<EntityRelation> meetingDiscussionList = mongoTemplate.find(query, EntityRelation.class);

		return meetingDiscussionList;
	}
	
	@Override
	public List<EntityRelation> findRelationByResourceId(String resourceId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("entityFrom.$id").is(new ObjectId(resourceId)).and("fromType").is(RelationType.RESOURCE));

		List<EntityRelation> meetingDiscussionList = mongoTemplate.find(query, EntityRelation.class);

		return meetingDiscussionList;
	}
}