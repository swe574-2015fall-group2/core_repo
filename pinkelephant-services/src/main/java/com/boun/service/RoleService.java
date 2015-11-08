package com.boun.service;

import com.boun.data.mongo.model.Role;
import com.boun.http.request.CreateRoleRequest;
import com.boun.http.request.UpdateRoleRequest;
import com.boun.http.response.ActionResponse;

import java.util.List;

public interface RoleService {

    ActionResponse createRole(CreateRoleRequest request);

    ActionResponse updateRole(UpdateRoleRequest request);

    List<Role> findAll();

    List<Role> findAll(List<String> ids);

    boolean delete(String id);
}
