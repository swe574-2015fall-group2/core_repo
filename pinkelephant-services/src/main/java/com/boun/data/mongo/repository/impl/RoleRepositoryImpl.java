package com.boun.data.mongo.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

public class RoleRepositoryImpl {

    private final static Logger logger = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @Autowired private MongoTemplate mongoTemplate;


}
