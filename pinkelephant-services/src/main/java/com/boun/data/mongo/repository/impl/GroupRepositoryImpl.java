package com.boun.data.mongo.repository.impl;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupCount;
import com.boun.data.mongo.model.GroupMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.boun.data.mongo.repository.custom.GroupRepositoryCustom;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class GroupRepositoryImpl implements GroupRepositoryCustom{

	private final static Logger logger = LoggerFactory.getLogger(GroupRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<Group> findLatestGroups() {
		Query query = new Query();
		query.limit(5);
		query.with(new Sort(Sort.Direction.DESC, "createdAt"));

		return mongoTemplate.find(query, Group.class);
	}
}
