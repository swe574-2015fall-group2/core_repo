package com.boun.data.mongo.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.repository.custom.MeetingRepositoryCustom;

public class MeetingRepositoryImpl implements MeetingRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Meeting> findMeetings(String groupId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("group.$id").is(new ObjectId(groupId)));

		List<Meeting> meetingList = mongoTemplate.find(query, Meeting.class);

		return meetingList;
	}
}
