package com.boun.data.mongo.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.repository.custom.GroupRepositoryCustom;

public class GroupRepositoryImpl implements GroupRepositoryCustom{

	private final static Logger logger = LoggerFactory.getLogger(GroupRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Group findByGroupName(String name) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		
		Group group = mongoTemplate.findOne(query, Group.class);
		
		return group;
	}
}
