package com.boun.service;

import java.util.List;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.response.ActionResponse;

public abstract class PinkElephantTaggedService extends PinkElephantService{

    protected abstract TagService getTagService();

    public abstract TaggedEntity findById(String entityId);
    
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
	
	public void updateTag(TaggedEntity taggedEntity, List<String> newList) {
		
		List<String> addedTagList = taggedEntity.addTagList(newList);
		if(addedTagList != null && !addedTagList.isEmpty()){
			for (String tag : addedTagList) {
				getTagService().tag(tag, taggedEntity, true);
			}	
		}

		List<String> removedTagList = taggedEntity.removeTagList(newList);
		if(removedTagList != null && !removedTagList.isEmpty()){
			for (String tag : removedTagList) {
				getTagService().tag(tag, taggedEntity, false);
			}	
		}
	}
}
