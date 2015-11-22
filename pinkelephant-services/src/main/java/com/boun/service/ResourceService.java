package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.Resource;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.request.BasicDeleteRequest;

public interface ResourceService {

    Resource findById(String resourceId);

    Resource createExternalResource(CreateResourceRequest request);

    Resource uploadResource(byte[] bytes, String name, String groupId, String authToken);

    List<Resource> queryResourcesOfGroup(BasicQueryRequest request);

    boolean delete(BasicDeleteRequest id);
    
}
