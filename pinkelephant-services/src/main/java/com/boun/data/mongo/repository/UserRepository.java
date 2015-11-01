package com.boun.data.mongo.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.User;

public interface UserRepository extends MongoRepository<User, Long>, UserRepositoryCustom {

    User findById(Long userId);

    User findByUsername(String userName);

    Set<User> findByIdIn(Collection<Long> ids);
    
}
