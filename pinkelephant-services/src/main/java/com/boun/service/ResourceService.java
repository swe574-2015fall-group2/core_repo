package com.boun.service;

import com.boun.data.mongo.model.Resource;

public interface ResourceService {

    Resource uploadResource(byte[] bytes, String name);

    boolean delete(String id);
    
}
