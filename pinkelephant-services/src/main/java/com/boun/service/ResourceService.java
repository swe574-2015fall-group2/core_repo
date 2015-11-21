package com.boun.service;

import com.boun.data.mongo.model.Resource;
import com.boun.http.request.DeleteResourceRequest;

public interface ResourceService {

    Resource findById(String resourceId);

    Resource uploadResource(byte[] bytes, String name, String authToken);

    boolean delete(DeleteResourceRequest id);
    
}
