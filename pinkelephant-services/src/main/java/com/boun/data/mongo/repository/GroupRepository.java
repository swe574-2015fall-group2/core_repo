package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.repository.custom.GroupRepositoryCustom;

public interface GroupRepository extends MongoRepository<Group, String>, GroupRepositoryCustom{

    Group findByName(String name);
}
