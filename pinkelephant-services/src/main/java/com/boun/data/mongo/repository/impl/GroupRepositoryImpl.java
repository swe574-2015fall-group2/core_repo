package com.boun.data.mongo.repository.impl;

import com.boun.data.mongo.model.GroupCount;
import com.boun.data.mongo.model.GroupMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import com.boun.data.mongo.repository.custom.GroupRepositoryCustom;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.List;

public class GroupRepositoryImpl implements GroupRepositoryCustom{

	private final static Logger logger = LoggerFactory.getLogger(GroupRepositoryImpl.class);

	@Autowired
	private MongoTemplate mongoTemplate;


	public void getPopularGroups() {
		Aggregation agg = newAggregation(
				group("group").count().as("total"),
				project("total").and("group").previousOperation(),
				sort(Sort.Direction.DESC, "total"));

		AggregationResults<GroupCount> groupResults
				= mongoTemplate.aggregate(agg, GroupMember.class, GroupCount.class);
		List<GroupCount> result = groupResults.getMappedResults();
	}
}
