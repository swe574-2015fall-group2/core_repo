package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.repository.custom.DiscussionRepositoryCustom;

public interface DiscussionRepository extends MongoRepository<Discussion, String>, DiscussionRepositoryCustom {

    
}
