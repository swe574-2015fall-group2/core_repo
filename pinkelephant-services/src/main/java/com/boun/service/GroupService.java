package com.boun.service;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupCount;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateUpdateGroupRequest;
import com.boun.http.request.JoinLeaveGroupRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UploadImageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetGroupResponse;
import com.boun.http.response.ListGroupResponse;

import java.util.List;

public interface GroupService {

	public Group findById(String groupId);

	public Group findByName(String groupName);

	public CreateResponse createGroup(CreateUpdateGroupRequest request);
	
	public ActionResponse updateGroup(CreateUpdateGroupRequest request);
	
	public ActionResponse joinGroup(JoinLeaveGroupRequest request);
	
	public ActionResponse leaveGroup(JoinLeaveGroupRequest request);
	
	public ListGroupResponse getMyGroups(BaseRequest request);
	
	public ListGroupResponse getAllGroups(BaseRequest request);

	public List<GroupCount> getPopularGroups(BaseRequest request);

	public List<Group> getLatestGroups(BaseRequest request);
	
	public ActionResponse uploadImage(UploadImageRequest request);
	
	public GetGroupResponse queryGroup(BasicQueryRequest request);
	
	public ActionResponse tag(TagRequest request);
}
