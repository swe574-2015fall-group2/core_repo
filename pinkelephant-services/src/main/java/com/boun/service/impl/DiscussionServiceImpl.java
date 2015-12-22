package com.boun.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.BaseEntity;
import com.boun.data.mongo.model.Comment;
import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.EntityRelation.RelationType;
import com.boun.data.mongo.repository.DiscussionRepository;
import com.boun.data.mongo.repository.EntityRelationRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateDiscussionRequest;
import com.boun.http.request.LinkRequest;
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
		discussion.setIsPinned(request.getIsPinned());

		discussionRepository.save(discussion);
		
		createRelation(discussion, RelationType.DISCUSSION, request.getMeetingIdList(), RelationType.MEETING);
		createRelation(discussion, RelationType.DISCUSSION, request.getResourceIdList(), RelationType.RESOURCE);
		
		tagService.tag(request.getTagList(), discussion, true);	
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}
	
	private void createRelation(BaseEntity from, RelationType fromType, List<String> idList, RelationType toType){
		if(idList == null || idList.isEmpty()){
			return;
		}
		for (String id : idList) {
			
			BaseEntity to = null;
			if(toType == RelationType.MEETING){
				to = meetingService.findById(id);				
			}else if(toType == RelationType.RESOURCE){
				to = resourceService.findById(id);
			}else if(toType == RelationType.DISCUSSION){
				to = findById(id);
			}
			
			EntityRelation meetingDiscussion = meetingDiscussionRepository.findRelation(from.getId(), fromType, to.getId(), toType);
			if(meetingDiscussion != null){
				throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_MEETING_DISCUSSION, "");
			}
			
			createRelation(from, fromType, to, toType);
		}
	}

	private void createRelation(BaseEntity from, RelationType fromType, BaseEntity to, RelationType toType){
		EntityRelation meetingDiscussion = new EntityRelation();
		meetingDiscussion.setEntityFrom(from);
		meetingDiscussion.setFromType(fromType);
		meetingDiscussion.setEntityTo(to);
		meetingDiscussion.setToType(toType);
		
		meetingDiscussionRepository.save(meetingDiscussion);
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
		
		createRelation(discussion, RelationType.DISCUSSION, request.getMeetingIdList(), RelationType.MEETING);
		createRelation(discussion, RelationType.DISCUSSION, request.getResourceIdList(), RelationType.RESOURCE);
		
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
			List<EntityRelation> meetingDiscussionList = meetingDiscussionRepository.findRelationByDiscussionId(discussion.getId());
			
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
		
		List<EntityRelation> meetingDiscussionList = meetingDiscussionRepository.findRelationByDiscussionId(discussion.getId());
		if(meetingDiscussionList != null && !meetingDiscussionList.isEmpty()){
			
			List<String> meetingIdList = new ArrayList<String>();
			List<String> resourceIdList = new ArrayList<String>();
			for (EntityRelation relation : meetingDiscussionList) {
				
				if(relation.getToType() == RelationType.MEETING){
					meetingIdList.add(relation.getEntityTo().getId());					
				}else if(relation.getToType() == RelationType.RESOURCE){
					resourceIdList.add(relation.getEntityTo().getId());
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
	
	public ActionResponse linkMeeting(LinkRequest request){
		validate(request);
		
		ActionResponse response = new ActionResponse();

		Discussion discussion = findById(request.getFromEntityId());
		Meeting meeting = meetingService.findById(request.getToEntityId());
		
		EntityRelation meetingDiscussion = meetingDiscussionRepository.findRelation(discussion.getId(), RelationType.DISCUSSION, meeting.getId(), RelationType.MEETING);
		if(meetingDiscussion != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_MEETING_DISCUSSION, "");
		}
		
		createRelation(discussion, RelationType.DISCUSSION, meeting, RelationType.MEETING);
		
		response.setAcknowledge(true);
		return response;
	}
	
	public ActionResponse linkResource(LinkRequest request){
		validate(request);
		
		ActionResponse response = new ActionResponse();

		Discussion discussion = findById(request.getFromEntityId());
		Resource resource  = resourceService.findById(request.getToEntityId());
		
		EntityRelation meetingDiscussion = meetingDiscussionRepository.findRelation(discussion.getId(), RelationType.DISCUSSION, resource.getId(), RelationType.RESOURCE);
		if(meetingDiscussion != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_MEETING_DISCUSSION, "");
		}
		
		createRelation(discussion, RelationType.DISCUSSION, resource, RelationType.RESOURCE);
		
		response.setAcknowledge(true);
		return response;
	}
	
	public ActionResponse removeResourceLink(LinkRequest request){
		validate(request);
		
		ActionResponse response = new ActionResponse();

		Discussion discussion = findById(request.getFromEntityId());
		Resource resource  = resourceService.findById(request.getToEntityId());
		
		EntityRelation meetingDiscussion = meetingDiscussionRepository.findRelation(discussion.getId(), RelationType.DISCUSSION, resource.getId(), RelationType.RESOURCE);
		if(meetingDiscussion == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_DISCUSSION_NOT_FOUND, "");
		}
		
		meetingDiscussionRepository.delete(meetingDiscussion);
		
		response.setAcknowledge(true);
		return response;
	}
	
	public ActionResponse removeMeetingLink(LinkRequest request){
		validate(request);
		
		ActionResponse response = new ActionResponse();

		Discussion discussion = findById(request.getFromEntityId());
		Meeting meeting = meetingService.findById(request.getToEntityId());
		
		EntityRelation meetingDiscussion = meetingDiscussionRepository.findRelation(discussion.getId(), RelationType.DISCUSSION, meeting.getId(), RelationType.MEETING);
		if(meetingDiscussion == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_DISCUSSION_NOT_FOUND, "");
		}
		
		meetingDiscussionRepository.delete(meetingDiscussion);
		
		response.setAcknowledge(true);
		return response;
	}
}