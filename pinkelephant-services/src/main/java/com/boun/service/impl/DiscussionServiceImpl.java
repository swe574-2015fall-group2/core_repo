package com.boun.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Comment;
import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.DiscussionRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateDiscussionRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateDiscussionRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetDiscussionResponse;
import com.boun.http.response.ListDiscussionResponse;
import com.boun.service.CommentService;
import com.boun.service.DiscussionService;
import com.boun.service.GroupService;
import com.boun.service.PinkElephantTaggedService;
import com.boun.service.TagService;

@Service
public class DiscussionServiceImpl extends PinkElephantTaggedService implements DiscussionService{

	@Autowired
	private DiscussionRepository discussionRepository;
	
	@Autowired
	private GroupService groupService;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private TagService tagService;
	
	@Override
	public Discussion findById(String discussionId) {
		
		Discussion discussion = discussionRepository.findOne(discussionId);
		if(discussion == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.DISCUSSION_NOT_FOUND, "");
		}
		return discussion;
	}
	
	public ActionResponse createDiscussion(CreateDiscussionRequest request){
		
		validate(request);
		
		Group group = groupService.findById(request.getGroupId());
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		Discussion discussion = new Discussion();
		discussion.setCreationTime(new Date());
		discussion.setDescription(request.getDescription());
		discussion.setGroup(group);
		discussion.setCreator(authenticatedUser);
		discussion.setName(request.getName());
		discussion.setTagList(request.getTagList());
		
		discussionRepository.save(discussion);
		
		tagService.tag(request.getTagList(), discussion, true);	
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}

	@Override
	public ActionResponse updateDiscussion(UpdateDiscussionRequest request) {
		
		validate(request);
		
		Discussion discussion = findById(request.getDiscussionId());
		Group group = groupService.findById(request.getGroupId());
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		if(!authenticatedUser.isEqual(discussion.getCreator())){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Discussion belongs to another user. Update not allowed !!!", "");
		}
		
		if(!discussion.getGroup().isEqual(group)){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Discussion belongs to another group. Update not allowed !!!", "");
		}
		
		discussion.setDescription(request.getDescription());
		discussion.setName(request.getName());
		discussion.setUpdateTime(new Date());
		discussionRepository.save(discussion);
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}
	
	public ListDiscussionResponse listDiscussions(BasicQueryRequest request){
		
		validate(request);
		
		Group group = groupService.findById(request.getId());
		
		//TODO check if authenticated user is a member of this group

		ListDiscussionResponse response = new ListDiscussionResponse();
		
		List<Discussion> discussionList = discussionRepository.findDiscussions(group.getId());
		if(discussionList == null || discussionList.isEmpty()){
			return response;
		}
		
		for (Discussion discussion : discussionList) {
			response.addDiscussion(discussion.getId(), discussion.getName(), discussion.getDescription(), discussion.getCreator().getId(), discussion.getCreationTime(), discussion.getTagList());
		}
		
		return response;
	}
	
	@Override
	public GetDiscussionResponse queryDiscussion(BasicQueryRequest request) {
		
		validate(request);
		
		Discussion discussion = findById(request.getId());
		
		GetDiscussionResponse response = new GetDiscussionResponse();
		
		response.setCreatorId(discussion.getCreator().getId());
		response.setDescription(discussion.getDescription());
		response.setGroupId(discussion.getGroup().getId());
		response.setId(discussion.getId());
		response.setName(discussion.getName());
		response.setTagList(discussion.getTagList());
		
		List<Comment> commentList = commentService.getComments(discussion.getId());
		if(commentList != null && !commentList.isEmpty()){
			for (Comment comment : commentList) {
				response.addComment(comment.getId(), comment.getComment(), comment.getCreationTime(), comment.getUser().getId());
			}
		}
		return response;
	}
	
	@Override
	public ActionResponse tag(TagRequest request) {
		
		validate(request);
		
		Discussion discussion = findById(request.getEntityId());
		
		ActionResponse response = new ActionResponse();
		if(tag(discussion, request.getTag(), request.isAdd())){
			response.setAcknowledge(true);
			
			discussionRepository.save(discussion);
		}
		
		return response;
	}

	@Override
	protected TagService getTagService() {
		return tagService;
	}

}
