package com.boun.service;

import com.boun.data.mongo.model.Resource;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.request.DeleteResourceRequest;

public interface ResourceService {

    Resource findById(String resourceId);

    Resource createExternalResource(CreateResourceRequest request);

    Resource uploadResource(byte[] bytes, String name, String groupId, String authToken);

    boolean delete(DeleteResourceRequest id);
    
}
