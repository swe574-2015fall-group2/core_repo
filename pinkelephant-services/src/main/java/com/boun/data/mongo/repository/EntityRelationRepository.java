package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.repository.custom.EntityRelationRepositoryCustom;

public interface EntityRelationRepository extends MongoRepository<EntityRelation, String>, EntityRelationRepositoryCustom{

}
