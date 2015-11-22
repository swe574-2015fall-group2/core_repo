package com.boun.data.mongo.repository.impl;

import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.repository.custom.ResourceRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class ResourceRepositoryImpl implements ResourceRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Resource> findResources(String groupId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("group.$id").is(new ObjectId(groupId)));

		List<Resource> resourceList = mongoTemplate.find(query, Resource.class);

		return resourceList;
	}
}
