package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.repository.custom.GroupMemberRepositoryCustom;

public interface GroupMemberRepository extends MongoRepository<GroupMember, String>, GroupMemberRepositoryCustom{

}
