package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.Role;
import com.boun.http.request.CreateRoleRequest;
import com.boun.http.request.UpdateRoleRequest;

public interface RoleService {

    Role findById(String id);

    Role createRole(CreateRoleRequest request);

    Role updateRole(UpdateRoleRequest request);

    List<Role> findAll();

    List<Role> findAll(List<String> ids);

    boolean delete(String id);
}
