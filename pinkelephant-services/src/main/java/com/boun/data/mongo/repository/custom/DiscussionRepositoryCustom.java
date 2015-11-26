package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.Discussion;

public interface DiscussionRepositoryCustom {

	public List<Discussion> findDiscussions(String groupId);

}
