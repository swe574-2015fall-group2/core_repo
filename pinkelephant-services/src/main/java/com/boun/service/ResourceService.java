package com.boun.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.ResourceResponse;

public interface ResourceService {

    Resource findById(String resourceId);

    List<Resource> findByIds(List<String> resourceIds);

    Resource createExternalResource(CreateResourceRequest request);

    ResourceResponse downloadResource(String resourceId) throws FileNotFoundException;

    Resource uploadResource(byte[] bytes, String name, String groupId, String authToken);

    List<Resource> queryResourcesOfGroup(QueryResourceRequest request);

    boolean delete(BasicDeleteRequest id);
    
    ActionResponse tag(TagRequest request);
    
	ActionResponse link(LinkRequest request, EntityType toType);
	
	ActionResponse removeLink(LinkRequest request, EntityType toType);
}
