package com.boun.data.mongo.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.mongo.repository.UserRepositoryCustom;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired private UserRepository userRepository;

    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public User loadById(Long id) {
        return null;
    }

	@Override
	public User findByUsernameAndPassword(String username, String password) {
		
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username).and("password").is(password));
		
		User user = mongoTemplate.findOne(query, User.class);
		
		return user;
	}

	@Override
	public User findByUsername(String username) {
		Query query = new Query();
		query.addCriteria(Criteria.where("username").is(username));
		
		User user = mongoTemplate.findOne(query, User.class);
		
		return user;
	}
}
