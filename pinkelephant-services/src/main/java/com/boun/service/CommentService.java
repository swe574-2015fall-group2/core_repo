package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.Comment;
import com.boun.http.request.AddCommentRequest;
import com.boun.http.response.ActionResponse;

public interface CommentService {

	public Comment findById(String commentId);

	public List<Comment> getComments(String discussionId);
	
	public ActionResponse createComment(AddCommentRequest request);
}
