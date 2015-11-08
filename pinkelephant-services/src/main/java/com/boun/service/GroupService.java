package com.boun.service;

import com.boun.http.request.CreateGroupRequest;
import com.boun.http.response.ActionResponse;

public interface GroupService {

	public ActionResponse createGroup(CreateGroupRequest request);
	
	public ActionResponse updateGroup(CreateGroupRequest request);
}
