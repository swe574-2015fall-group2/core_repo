package com.boun.data.mongo.repository.impl;

import com.boun.data.mongo.model.UserMetadata;
import com.boun.data.mongo.repository.UserMetadataRepositoryCustom;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserMetadataRepositoryImpl implements UserMetadataRepositoryCustom {

    @Autowired private MongoTemplate mongoTemplate;

}
