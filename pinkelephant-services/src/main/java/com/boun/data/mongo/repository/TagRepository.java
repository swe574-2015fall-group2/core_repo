package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

}
