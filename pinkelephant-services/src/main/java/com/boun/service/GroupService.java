package com.boun.service;

import com.boun.data.mongo.model.Group;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.CreateUpdateGroupRequest;
import com.boun.http.request.JoinLeaveGroupRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.ListGroupResponse;

public interface GroupService {

	public Group findById(String groupId);

	public Group findByName(String groupName);

	public CreateResponse createGroup(CreateUpdateGroupRequest request);
	
	public ActionResponse updateGroup(CreateUpdateGroupRequest request);
	
	public ActionResponse joinGroup(JoinLeaveGroupRequest request);
	
	public ActionResponse leaveGroup(JoinLeaveGroupRequest request);
	
	public ListGroupResponse getMyGroups(BaseRequest request);
	
	public ListGroupResponse getAllGroups(BaseRequest request);
}
