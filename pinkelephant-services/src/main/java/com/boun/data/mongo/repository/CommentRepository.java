package com.boun.data.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Comment;
import com.boun.data.mongo.repository.custom.CommentRespositoryCustom;

public interface CommentRepository extends MongoRepository<Comment, String>, CommentRespositoryCustom {

    
}
