package com.boun.service;

import com.boun.data.mongo.model.Role;
import com.boun.http.request.CreateRoleRequest;
import com.boun.http.response.ActionResponse;

import java.util.List;

public interface SecurityService {

    ActionResponse createRole(CreateRoleRequest request);

    List<Role> findAll();
}
