package com.boun.data.mongo.repository.impl;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.repository.custom.GroupMemberRepositoryCustom;

public class GroupMemberRepositoryImpl implements GroupMemberRepositoryCustom{

	private final static Logger logger = LoggerFactory.getLogger(GroupMemberRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public GroupMember findGroupMember(String userId, String groupId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("group.$id").is(new ObjectId(groupId)).and("user.$id").is(new ObjectId(userId)));
		
		GroupMember groupMember = mongoTemplate.findOne(query, GroupMember.class);
		
		return groupMember;
	}
}
