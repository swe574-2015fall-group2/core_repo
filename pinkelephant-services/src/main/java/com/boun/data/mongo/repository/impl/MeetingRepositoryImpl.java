package com.boun.data.mongo.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.repository.custom.MeetingRepositoryCustom;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MeetingRepositoryImpl implements MeetingRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Meeting> findMeetings(String groupId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("group.$id").is(new ObjectId(groupId)));

		List<Meeting> meetingList = mongoTemplate.find(query, Meeting.class);

		return meetingList;
	}
	
	public List<String> getMeetingsOfUser(String userId){
		
	    DBObject query = new BasicDBObject("invitedUserSet.$id", new ObjectId(userId));
	    DBCollection meetingCollection = mongoTemplate.getCollection("meeting");
	    
	    DBCursor cursor = meetingCollection.find(query);
	
		List<String> meetingIdList = new ArrayList<String>();
		while (cursor.hasNext()) {
			BasicDBObject meeting = (BasicDBObject)cursor.next();
			String id = meeting.getString("_id");
			meetingIdList.add(id);
		}
		cursor.close();
		
		return meetingIdList;
	}
}