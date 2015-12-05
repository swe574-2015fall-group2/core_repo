package com.boun.service;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.http.request.BaseRequest;

public abstract class PinkElephantTaggedService extends PinkElephantService{

    protected abstract TagService getTagService();

    protected void validate(String authToken) throws PinkElephantRuntimeException{

        BaseRequest request = new BaseRequest();
        request.setAuthToken(authToken);

        validate(request);
    }
    
	public boolean tag(TaggedEntity entity, String tag, boolean add) {
		
		if(entity.updateTagList(tag, add)){
			getTagService().tag(tag, entity, add);
			return true;
		}
		
		return false;
	}
}
