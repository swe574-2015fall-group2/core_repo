package com.boun.data.mongo.repository.impl;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.util.ObjectUtils;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.mongo.repository.UserRepositoryCustom;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final static Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Autowired private UserRepository userRepository;

    @Autowired private MongoTemplate mongoTemplate;

    @Override
    public User loadById(Long id) {
        return null;
    }
}
