package com.boun.service;

import com.boun.data.mongo.model.Discussion;

public interface DiscussionService {

	public Discussion findById(String discussionId);
}
