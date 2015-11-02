package com.boun.service;

import com.boun.http.request.CreateRoleRequest;
import com.boun.http.response.ActionResponse;

public interface SecurityService {

    ActionResponse createRole(CreateRoleRequest request);
}
