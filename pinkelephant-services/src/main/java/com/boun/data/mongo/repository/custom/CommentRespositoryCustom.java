package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.Comment;

public interface CommentRespositoryCustom {

	public List<Comment> findComments(String discussionId);
}
