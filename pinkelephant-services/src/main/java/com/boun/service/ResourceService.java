package com.boun.service;

import com.boun.http.response.CreateResourceResponse;

public interface ResourceService {

    public CreateResourceResponse uploadResource(byte[] bytes, String name);
    
}
