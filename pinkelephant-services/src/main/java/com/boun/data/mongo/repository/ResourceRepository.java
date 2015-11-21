package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.Resource;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<Resource, String> {

}
