package com.boun.data.mongo.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.custom.UserRepositoryCustom;

public interface UserRepository extends MongoRepository<User, String>, UserRepositoryCustom {

    User findById(String userId);

    User findByUsername(String userName);

    Set<User> findByIdIn(Collection<String> ids);
    
}
