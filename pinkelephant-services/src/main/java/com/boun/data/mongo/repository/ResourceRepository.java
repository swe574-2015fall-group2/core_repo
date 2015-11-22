package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.repository.custom.ResourceRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceRepository extends MongoRepository<Resource, String>, ResourceRepositoryCustom {

}
