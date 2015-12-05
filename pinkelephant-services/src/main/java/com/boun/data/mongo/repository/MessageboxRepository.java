package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Messagebox;
import com.boun.data.mongo.repository.custom.MessageboxRepositoryCustom;

public interface MessageboxRepository extends MongoRepository<Messagebox, String>, MessageboxRepositoryCustom {

}
