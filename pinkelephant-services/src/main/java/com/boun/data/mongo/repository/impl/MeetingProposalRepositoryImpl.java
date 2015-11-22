package com.boun.data.mongo.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.MeetingProposal;
import com.boun.data.mongo.repository.custom.MeetingProposalRepositoryCustom;

public class MeetingProposalRepositoryImpl implements MeetingProposalRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(MeetingProposalRepositoryImpl.class);

    @Autowired private MongoTemplate mongoTemplate;

	public List<MeetingProposal> findMeetingProposals(String discussionId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("discussion.$id").is(new ObjectId(discussionId)).and("status").is(true));

		List<MeetingProposal> meetingList = mongoTemplate.find(query, MeetingProposal.class);

		return meetingList;
	}

}
