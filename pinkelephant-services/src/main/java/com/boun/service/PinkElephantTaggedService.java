package com.boun.service;

import org.springframework.data.mongodb.repository.MongoRepository;

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
}
