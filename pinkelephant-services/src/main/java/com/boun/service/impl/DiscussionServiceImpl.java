package com.boun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.repository.DiscussionRepository;
import com.boun.service.DiscussionService;
import com.boun.service.PinkElephantService;

@Service
public class DiscussionServiceImpl extends PinkElephantService implements DiscussionService{

	@Autowired
	private DiscussionRepository discussionRepository;
	
	@Override
	public Discussion findById(String discussionId) {
		
		Discussion discussion = discussionRepository.findOne(discussionId);
		if(discussion == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.DISCUSSION_NOT_FOUND, "");
		}
		return discussion;
	}

}
