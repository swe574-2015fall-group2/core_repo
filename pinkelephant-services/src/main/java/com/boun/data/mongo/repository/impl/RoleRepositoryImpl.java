package com.boun.data.mongo.repository.impl;

import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.mongo.repository.UserRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class RoleRepositoryImpl {

    private final static Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Autowired private MongoTemplate mongoTemplate;


}
