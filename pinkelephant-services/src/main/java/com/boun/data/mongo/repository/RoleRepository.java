package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, Long> {

    Role findById(Long roleId);

    Role findByName(String name);
    
}
