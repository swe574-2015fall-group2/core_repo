package com.boun.data.mongo.repository.impl;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.Messagebox;
import com.boun.data.mongo.repository.custom.MessageboxRepositoryCustom;

public class MessageboxRepositoryImpl implements MessageboxRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(MessageboxRepositoryImpl.class);

    @Autowired private MongoTemplate mongoTemplate;

	@Override
	public Messagebox search(String senderId, String receiverId) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("receiver.$id").is(new ObjectId(receiverId)).and("sender.$id").is(new ObjectId(senderId)));

		Messagebox messagebox = mongoTemplate.findOne(query, Messagebox.class);

		return messagebox;
	}

	@Override
	public List<Messagebox> searchBySender(String senderId) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("sender.$id").is(new ObjectId(senderId)));

		List<Messagebox> messageboxList = mongoTemplate.find(query, Messagebox.class);

		return messageboxList;
	}

	@Override
	public List<Messagebox> searchByReceiver(String receiverId) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("receiver.$id").is(new ObjectId(receiverId)));

		List<Messagebox> messageboxList = mongoTemplate.find(query, Messagebox.class);

		return messageboxList;
	}
}
