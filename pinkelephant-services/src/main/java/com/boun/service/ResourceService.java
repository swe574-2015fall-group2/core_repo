package com.boun.service;

import com.boun.data.mongo.model.Resource;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.request.DeleteResourceRequest;
import com.boun.http.request.QueryResourceRequest;

import java.util.List;

public interface ResourceService {

    Resource findById(String resourceId);

    Resource createExternalResource(CreateResourceRequest request);

    Resource uploadResource(byte[] bytes, String name, String groupId, String authToken);

    List<Resource> queryResourcesOfGroup(QueryResourceRequest request);

    boolean delete(DeleteResourceRequest id);
    
}
