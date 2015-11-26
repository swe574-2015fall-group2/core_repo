package com.boun.data.mongo.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Comment;
import com.boun.data.mongo.repository.custom.CommentRespositoryCustom;

public class CommentRepositoryImpl implements CommentRespositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);

    @Autowired private MongoTemplate mongoTemplate;

	public List<Comment> findComments(String discussionId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("discussion.$id").is(new ObjectId(discussionId)));

		List<Comment> meetingList = mongoTemplate.find(query, Comment.class);

		return meetingList;
	}
}
