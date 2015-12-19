package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Tag;
import com.boun.data.mongo.repository.custom.TagRepositoryCustom;

public interface TagRepository extends MongoRepository<Tag, String>, TagRepositoryCustom {

}
