package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRepository extends MongoRepository<User, Long>, UserRepositoryCustom {

    User findById(Long userId);

    User findByUsername(String userName);

    Set<User> findByIdIn(Collection<Long> ids);
}
