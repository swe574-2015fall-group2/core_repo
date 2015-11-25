package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.enums.GroupStatus;
import com.boun.data.common.enums.MemberStatus;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.GroupMemberRepository;
import com.boun.data.mongo.repository.GroupRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateUpdateGroupRequest;
import com.boun.http.request.JoinLeaveGroupRequest;
import com.boun.http.request.UploadImageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetGroupResponse;
import com.boun.http.response.ListGroupResponse;
import com.boun.service.GroupService;
import com.boun.service.PinkElephantService;
import com.boun.service.TagService;
import com.boun.util.ImageUtil;

@Service
public class GroupServiceImpl extends PinkElephantService implements GroupService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TagService tagService;
	
	@Override
	public Group findById(String groupId) {
		Group group = groupRepository.findOne(groupId);

		if(group == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		return group;
	}

	@Override
	public Group findByName(String groupName) {
		Group group = groupRepository.findByName(groupName);

		if(group == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		return group;
	}

	@Override
	public CreateResponse createGroup(CreateUpdateGroupRequest request) {
		
		validate(request);
		
		CreateResponse response = new CreateResponse();
		if(request.getName() == null || "".equalsIgnoreCase(request.getName())){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "Group name is empty", "");
		}
		
		Group group = groupRepository.findByName(request.getName());
		if(group != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_GROUP, "");
		}

		group = new Group();
		group.setName(request.getName());
		group.setDescription(request.getDescription());
		group.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		group.setStatus(GroupStatus.ACTIVE);
		
		groupRepository.save(group);
		
		response.setAcknowledge(true);
		response.setEntityId(group.getId());

		tagService.tag("test", group);
		
		return response;
	}
	
	@Override
	public ActionResponse uploadImage(UploadImageRequest request){
		
		validate(request);
		
		Group group = findById(request.getEntityId());
		
		String imagePath = ImageUtil.saveImage("User", request);

		group.setImagePath(imagePath);
		groupRepository.save(group);
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}
	
	@Override
	public ActionResponse updateGroup(CreateUpdateGroupRequest request) {

		validate(request);

		ActionResponse response = new ActionResponse();
		Group group = groupRepository.findByName(request.getName());
		if(group == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}
		group.setDescription(request.getDescription());
		
		groupRepository.save(group);
		
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ActionResponse joinGroup(JoinLeaveGroupRequest request) {

		validate(request);

		ActionResponse response = new ActionResponse();
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		Group group = groupRepository.findOne(request.getGroupId());
		if(group == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		User user = userRepository.findOne(authenticatedUser.getId());
		if(user == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
		}

		GroupMember groupMember = groupMemberRepository.findGroupMember(user.getId(), group.getId());
		if(groupMember != null){
			
			if(groupMember.getStatus().value() == MemberStatus.ACTIVE.value()){
				throw new PinkElephantRuntimeException(400, ErrorCode.USER_IS_ALREADY_A_MEMBER, "");
			}
			
			if(groupMember.getStatus().value() == MemberStatus.BLOCKED.value()){
				throw new PinkElephantRuntimeException(400, ErrorCode.USER_IS_BLOCKED, "");
			}
			
			groupMember.setStatus(MemberStatus.ACTIVE);
		}else{
			groupMember = new GroupMember();
			groupMember.setGroup(group);
			groupMember.setUser(user);
			groupMember.setStatus(MemberStatus.ACTIVE);
		}
		
		groupMemberRepository.save(groupMember);
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ActionResponse leaveGroup(JoinLeaveGroupRequest request) {

		validate(request);

		ActionResponse response = new ActionResponse();
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());

		Group group = groupRepository.findOne(request.getGroupId());
		if(group == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		GroupMember groupMember = groupMemberRepository.findGroupMember(authenticatedUser.getId(), request.getGroupId());
		if(groupMember == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_IS_NOT_A_MEMBER, "");
		}
		
		groupMemberRepository.delete(groupMember);
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ListGroupResponse getMyGroups(BaseRequest request) {
		
		validate(request);
		
		ListGroupResponse response = new ListGroupResponse();
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());

		List<Group> groupList = groupMemberRepository.findGroupsOfUser(authenticatedUser.getId());
		if(groupList == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		for (Group group : groupList) {
			response.addGroup(group.getId(), group.getName(), group.getDescription());
		}
		
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public GetGroupResponse queryGroup(BasicQueryRequest request) {
		
		validate(request);
		
		Group group = findById(request.getId());
		
		GetGroupResponse response = new GetGroupResponse();
		response.setDescription(group.getDescription());
		response.setId(group.getId());
		response.setName(group.getName());
		response.setImage(ImageUtil.getImage(group.getImagePath()));
		
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ListGroupResponse getAllGroups(BaseRequest request) {
		
		validate(request);
		
		//TODO group all groups as popular, my groups, others etc..
		
		ListGroupResponse response = new ListGroupResponse();
		List<Group> groupList = groupRepository.findAll();
		if(groupList == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		for (Group group : groupList) {
			if(group.getStatus() == null || group.getStatus().value() != GroupStatus.ACTIVE.value()){
				continue;
			}
			response.addGroup(group.getId(), group.getName(), group.getDescription());
		}
		
		response.setAcknowledge(true);

		return response;
	}
	

}
