package com.boun.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Comment;
import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.CommentRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.AddCommentRequest;
import com.boun.http.response.ActionResponse;
import com.boun.service.CommentService;
import com.boun.service.DiscussionService;
import com.boun.service.PinkElephantService;

@Service
public class CommentServiceImpl extends PinkElephantService implements CommentService{

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private DiscussionService discussionService;
	
	@Override
	public Comment findById(String commentId) {
		
		Comment comment = commentRepository.findOne(commentId);
		if(comment == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.COMMENT_NOT_FOUND, "");
		}
		return comment;
	}
	
	public List<Comment> getComments(String discussionId) {
		
		return commentRepository.findComments(discussionId);
	}
		
	public ActionResponse createComment(AddCommentRequest request){
		
		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		//TODO check if authenticated user is a member of this group
		
		Discussion discussion = discussionService.findById(request.getDiscussionId());
		
		Comment comment = new Comment();
		comment.setComment(request.getComment());
		comment.setCreationTime(new Date());
		comment.setDiscussion(discussion);
		comment.setUser(authenticatedUser);
		
		commentRepository.save(comment);
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}

}
