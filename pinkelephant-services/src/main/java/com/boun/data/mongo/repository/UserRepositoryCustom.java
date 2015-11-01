package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.User;

public interface UserRepositoryCustom {

    User loadById(Long id);

    User findByUsernameAndPassword(String username, String password);
    
    User findByUsername(String username);
    
    User findByOneTimeToken(String oneTimeToken);

}
