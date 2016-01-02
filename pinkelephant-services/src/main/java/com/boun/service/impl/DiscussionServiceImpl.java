package com.boun.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boun.data.mongo.model.*;
import com.boun.http.request.BasicDeleteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.data.mongo.repository.DiscussionRepository;
import com.boun.data.mongo.repository.EntityRelationRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateDiscussionRequest;
import com.boun.http.request.UpdateDiscussionRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetDiscussionResponse;
import com.boun.http.response.ListDiscussionResponse;
import com.boun.service.CommentService;
import com.boun.service.DiscussionService;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.PinkElephantTaggedService;
import com.boun.service.ResourceService;
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
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired EntityRelationRepository meetingDiscussionRepository;
	
	@Override
	public Discussion findById(String discussionId) {
		
		Discussion discussion = discussionRepository.findOne(discussionId);
		if(discussion == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.DISCUSSION_NOT_FOUND, "");
		}
		return discussion;
	}
	
	@Override
	public void save(TaggedEntity entity) {
		discussionRepository.save((Discussion)entity);
	}

	public void delete(Discussion entity) {
		discussionRepository.delete(entity);
	}

	@Override
	public boolean archiveDiscussion(BasicDeleteRequest request) {
		Discussion discussion = discussionRepository.findOne(request.getId());

		delete(discussion);

		return true;
	}


	public ActionResponse createDiscussion(CreateDiscussionRequest request){
		
		validate(request);

		List<Resource> resources = null;
		if(request.getResourceIdList() != null)
			resources = resourceService.findByIds(request.getResourceIdList());

		Group group = groupService.findById(request.getGroupId());
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		Discussion discussion = new Discussion();
		discussion.setCreationTime(new Date());
		discussion.setDescription(request.getDescription());
		discussion.setGroup(group);
		discussion.setCreator(authenticatedUser);
		discussion.setName(request.getName());
		discussion.setTagList(request.getTagList());
		discussion.setIsPinned(request.getIsPinned());
		discussion.setResources(resources);

		discussionRepository.save(discussion);
		
		List<EntityRelation> entityRelationList = findRelationById(discussion.getId());
		
		createRelation(discussion, request.getMeetingIdList(), EntityType.MEETING, entityRelationList);
		
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
		discussion.setIsPinned(request.getIsPinned());
		updateTag(discussion, request.getTagList());
		
		discussionRepository.save(discussion);
		
		List<EntityRelation> entityRelationList = findRelationById(discussion.getId());
		
		createRelation(discussion, request.getMeetingIdList(), EntityType.MEETING, entityRelationList);
		createRelation(discussion, request.getResourceIdList(), EntityType.RESOURCE, entityRelationList);
		
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
			List<EntityRelation> meetingDiscussionList = findRelationById(discussion.getId());
			
			response.addDiscussion(discussion.getId(), discussion.getName(), discussion.getDescription(), discussion.getCreator().getId(), discussion.getCreationTime(), discussion.getTagList(), discussion.getIsPinned(), meetingDiscussionList);
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
		
		List<EntityRelation> meetingDiscussionList = findRelationById(discussion.getId());
		if(meetingDiscussionList != null && !meetingDiscussionList.isEmpty()){
			
			List<String> meetingIdList = new ArrayList<String>();
			List<String> resourceIdList = new ArrayList<String>();
			for (EntityRelation relation : meetingDiscussionList) {
				
				if(relation.getMeeting() != null){
					meetingIdList.add(relation.getMeeting().getId());					
				}else if(relation.getResource() != null){
					resourceIdList.add(relation.getResource().getId());
				}
			}
			
			response.setMeetingIdList(meetingIdList);
			response.setResourceIdList(meetingIdList);
		}
		
		List<Comment> commentList = commentService.getComments(discussion.getId());
		if(commentList != null && !commentList.isEmpty()){
			for (Comment comment : commentList) {
				response.addComment(comment.getId(), comment.getComment(), comment.getCreationTime(), comment.getUser().getId());
			}
		}
		return response;
	}

	@Override
	protected TagService getTagService() {
		return tagService;
	}
	
	@Override
	public List<EntityRelation> findRelationById(String meetindId) {

		return getEntityRelationRepository().findRelationByDiscussionId(meetindId);
	}

	@Override
	protected ResourceService getResourceService() {
		return resourceService;
	}

	@Override
	protected MeetingService getMeetingService() {
		return meetingService;
	}

	@Override
	protected DiscussionService getDiscussionService() {
		return this;
	}

	@Override
	protected EntityRelationRepository getEntityRelationRepository() {
		return meetingDiscussionRepository;
	}
}