package com.boun.data.mongo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.boun.data.mongo.model.MeetingProposal;
import com.boun.data.mongo.repository.custom.MeetingProposalRepositoryCustom;

public interface MeetingProposalRepository extends MongoRepository<MeetingProposal, String>, MeetingProposalRepositoryCustom {

    
}
