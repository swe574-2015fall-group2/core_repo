package com.boun.service;

import com.boun.data.mongo.model.Group;
import com.boun.http.request.CreateGroupRequest;
import com.boun.http.request.UpdateGroupRequest;
import com.boun.http.response.ActionResponse;

public interface GroupService {

	public Group findById(String groupId);

	public ActionResponse createGroup(CreateGroupRequest request);
	
	public ActionResponse updateGroup(UpdateGroupRequest request);
}
