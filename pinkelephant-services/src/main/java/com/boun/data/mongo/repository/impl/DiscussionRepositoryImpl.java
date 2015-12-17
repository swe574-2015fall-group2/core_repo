package com.boun.data.mongo.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.repository.custom.DiscussionRepositoryCustom;

public class DiscussionRepositoryImpl implements DiscussionRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(DiscussionRepositoryImpl.class);

    @Autowired private MongoTemplate mongoTemplate;

	public List<Discussion> findDiscussions(String groupId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("group.$id").is(new ObjectId(groupId)));
		query.with(new Sort(Sort.Direction.DESC, "isPinned"));

		List<Discussion> discussionList = mongoTemplate.find(query, Discussion.class);

		return discussionList;
	}
}
