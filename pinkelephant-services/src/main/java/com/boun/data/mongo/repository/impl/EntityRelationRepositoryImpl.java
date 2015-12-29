package com.boun.data.mongo.repository.impl;

import java.util.List;

import javax.management.relation.RelationType;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.repository.custom.EntityRelationRepositoryCustom;

public class EntityRelationRepositoryImpl implements EntityRelationRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<EntityRelation> findRelationByMeetingId(String meetingId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("meeting.$id").is(new ObjectId(meetingId)));

		List<EntityRelation> meetingDiscussionList = mongoTemplate.find(query, EntityRelation.class);

		return meetingDiscussionList;
	}
	
	@Override
	public List<EntityRelation> findRelationByDiscussionId(String discussionId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("discussion.$id").is(new ObjectId(discussionId)));

		List<EntityRelation> meetingDiscussionList = mongoTemplate.find(query, EntityRelation.class);

		return meetingDiscussionList;
	}
	
	@Override
	public List<EntityRelation> findRelationByResourceId(String resourceId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("resource.$id").is(new ObjectId(resourceId)));

		List<EntityRelation> meetingDiscussionList = mongoTemplate.find(query, EntityRelation.class);

		return meetingDiscussionList;
	}
}