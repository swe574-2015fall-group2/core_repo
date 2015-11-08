package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.repository.custom.MeetingRepositoryCustom;

public interface MeetingRepository extends MongoRepository<Meeting, String>, MeetingRepositoryCustom{

}
