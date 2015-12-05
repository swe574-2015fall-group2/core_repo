package com.boun.data.mongo.repository.impl;

import java.util.ArrayList;
import java.util.List;

import com.boun.data.mongo.model.GroupCount;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.repository.custom.GroupMemberRepositoryCustom;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

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
	
	@Override
	public List<Group> findGroupsOfUser(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("user.$id").is(new ObjectId(userId)));
		
		List<GroupMember> groupMembers = mongoTemplate.find(query, GroupMember.class);
		
		List<Group> resultList = new ArrayList<Group>();
		for (GroupMember groupMember : groupMembers) {
			resultList.add(groupMember.getGroup());
		}
		return resultList;
	}

	@Override
	public List<GroupCount> findPopularGroups() {

		Aggregation agg = newAggregation(
				group("group").count().as("total"),
				limit(5),
				project("total").and("group").previousOperation(),
				sort(Sort.Direction.DESC, "total"));

		AggregationResults<GroupCount> groupResults
				= mongoTemplate.aggregate(agg, GroupMember.class, GroupCount.class);
		List<GroupCount> result = groupResults.getMappedResults();

		return result;
	}
}
