package com.boun.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.data.mongo.repository.EntityRelationRepository;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.LinkRequest;
import com.boun.http.request.TagData;
import com.boun.http.request.TagRequest;
import com.boun.http.response.ActionResponse;

public abstract class PinkElephantTaggedService extends PinkElephantService{

    protected abstract TagService getTagService();
    protected abstract ResourceService getResourceService();
    protected abstract MeetingService getMeetingService();
    protected abstract DiscussionService getDiscussionService();
    
    protected abstract EntityRelationRepository getEntityRelationRepository();

    public abstract TaggedEntity findById(String entityId);
    public abstract List<EntityRelation> findRelationById(String meetindId);
    
    public abstract void save(TaggedEntity entity);
    
    protected void validate(String authToken) throws PinkElephantRuntimeException{

        BaseRequest request = new BaseRequest();
        request.setAuthToken(authToken);

        validate(request);
    }
    
	public ActionResponse tag(TagRequest request) {
		
		validate(request);
		
		TaggedEntity taggedEntity = findById(request.getEntityId());
		
		ActionResponse response = new ActionResponse();

		if(taggedEntity.updateTagList(request.getTag(), request.isAdd())){
			getTagService().tag(request.getTag(), taggedEntity, request.isAdd());
			response.setAcknowledge(true);
		
			save(taggedEntity);
		}
		
		return response;
	}
	
	public void updateTag(TaggedEntity taggedEntity, List<TagData> newList) {
		
		List<TagData> addedTagList = taggedEntity.addTagList(newList);
		if(addedTagList != null && !addedTagList.isEmpty()){
			for (TagData tag : addedTagList) {
				getTagService().tag(tag, taggedEntity, true);
			}	
		}

		List<TagData> removedTagList = taggedEntity.removeTagList(newList);
		if(removedTagList != null && !removedTagList.isEmpty()){
			for (TagData tag : removedTagList) {
				getTagService().tag(tag, taggedEntity, false);
			}	
		}
	}
	
	public ActionResponse link(LinkRequest request, EntityType toType){
		validate(request);
		
		ActionResponse response = new ActionResponse();

		List<EntityRelation> entityRelationList = findRelationById(request.getFromEntityId());
		if(exists(entityRelationList, request.getToEntityId(), toType)){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_MEETING_DISCUSSION, "");
		}
		
		List<String> idList = new ArrayList<String>();
		idList.add(request.getToEntityId());
		
		createRelation(findById(request.getFromEntityId()), idList, toType, entityRelationList);
		
		response.setAcknowledge(true);
		return response;
	}
	
	public ActionResponse removeLink(LinkRequest request, EntityType toType){
		validate(request);
		
		ActionResponse response = new ActionResponse();

		List<EntityRelation> entityRelationList = findRelationById(request.getFromEntityId());
		for (Iterator<EntityRelation> iterator = entityRelationList.iterator(); iterator.hasNext();) {
			EntityRelation entityRelation = iterator.next();
			
			boolean found = false;
			if(toType == EntityType.RESOURCE && entityRelation.getResource() != null && entityRelation.getResource().getId().equalsIgnoreCase(request.getToEntityId())){
				found = true;
			}else if(toType == EntityType.DISCUSSION && entityRelation.getDiscussion() != null && entityRelation.getDiscussion().getId().equalsIgnoreCase(request.getToEntityId())){
				found = true;
			}else if(toType == EntityType.MEETING && entityRelation.getMeeting() != null && entityRelation.getMeeting().getId().equalsIgnoreCase(request.getToEntityId())){
				found = true;
			}
			
			if(found){
				getEntityRelationRepository().delete(entityRelation);
				break;
			}
		}
		
		response.setAcknowledge(true);
		return response;
	}
	

	
	protected void createRelation(TaggedEntity from, List<String> idList, EntityType toType, List<EntityRelation> entityRelationList){
		if(idList == null || idList.isEmpty()){
			return;
		}
		
		for (String id : idList) {
			
			if(exists(entityRelationList, id, toType)){
				continue;
			}
			
			createRelation(from, id, toType);
		}
	}
	
	private boolean exists(List<EntityRelation> entityRelationList, String id, EntityType type){
		
		for (EntityRelation entityRelation : entityRelationList) {
		
			boolean found = false;
			if(type == EntityType.DISCUSSION){
				
				found = entityRelation.getDiscussion() == null ? false : entityRelation.getDiscussion().getId().equalsIgnoreCase(id);
				
			}else if(type == EntityType.MEETING){
				
				found = entityRelation.getMeeting() == null ? false : entityRelation.getMeeting().getId().equalsIgnoreCase(id);
				
			}else if(type == EntityType.RESOURCE){
				
				found = entityRelation.getResource() == null ? false : entityRelation.getResource().getId().equalsIgnoreCase(id);
			}
			
			if(found){
				return true;
			}
		}
		return false;
	}
	
	private void createRelation(TaggedEntity from, String id, EntityType toType){
		EntityRelation meetingDiscussion = new EntityRelation();
		
		if(toType == EntityType.MEETING){
			meetingDiscussion.setMeeting(getMeetingService().findById(id));
		}else if(toType == EntityType.RESOURCE){
			meetingDiscussion.setResource(getResourceService().findById(id));
		}else if(toType == EntityType.DISCUSSION){
			meetingDiscussion.setDiscussion(getDiscussionService().findById(id));
		}
		
		if(from.getEntityType() == EntityType.MEETING){
			meetingDiscussion.setMeeting((Meeting)from);
		}else if(from.getEntityType() == EntityType.RESOURCE){
			meetingDiscussion.setResource((Resource)from);
		}else if(from.getEntityType() == EntityType.DISCUSSION){
			meetingDiscussion.setDiscussion((Discussion)from);
		}

		getEntityRelationRepository().save(meetingDiscussion);
	}
}
