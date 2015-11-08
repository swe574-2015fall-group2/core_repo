package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.GroupStatus;
import com.boun.data.common.MemberStatus;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.GroupMemberRepository;
import com.boun.data.mongo.repository.GroupRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.CreateUpdateGroupRequest;
import com.boun.http.request.JoinLeaveGroupRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.ListGroupResponse;
import com.boun.service.GroupService;
import com.boun.service.PinkElephantService;

@Service
public class GroupServiceImpl extends PinkElephantService implements GroupService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Group findById(String groupId) {
		Group group = groupRepository.findOne(groupId);

		if(group == null) {
			throw new PinkElephantRuntimeException(400, "400", "Couldn't find group", "");
		}

		return group;
	}

	@Override
	public CreateResponse createGroup(CreateUpdateGroupRequest request) {
		CreateResponse response = new CreateResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			if(request.getName() == null || "".equalsIgnoreCase(request.getName())){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.INVALID_INPUT.format("Group name is empty"));
				return response;
			}
			
			Group group = groupRepository.findByGroupName(request.getName());
			if(group != null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.DUPLICATE_GROUP.getMessage());
				return response;
			}

			group = new Group();
			group.setName(request.getName());
			group.setDescription(request.getDescription());
			group.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
			group.setStatus(GroupStatus.ACTIVE);
			
			groupRepository.save(group);
			
			response.setAcknowledge(true);
			response.setEntityId(group.getId());
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createGroup()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse updateGroup(CreateUpdateGroupRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			Group group = groupRepository.findByGroupName(request.getName());
			if(group == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}
			group.setDescription(request.getDescription());
			
			groupRepository.save(group);
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in updateGroup()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse joinGroup(JoinLeaveGroupRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		try {
			Group group = groupRepository.findOne(request.getGroupId());
			if(group == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}

			User user = userRepository.findOne(authenticatedUser.getId());
			if(user == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.USER_NOT_FOUND.getMessage());
				return response;
			}

			GroupMember groupMember = groupMemberRepository.findGroupMember(user.getId(), group.getId());
			if(groupMember != null){
				
				if(groupMember.getStatus().value() == MemberStatus.ACTIVE.value()){
					response.setAcknowledge(false);
					response.setMessage(ErrorCode.USER_IS_ALREADY_A_MEMBER.getMessage());
					return response;	
				}
				
				if(groupMember.getStatus().value() == MemberStatus.BLOCKED.value()){
					response.setAcknowledge(false);
					response.setMessage(ErrorCode.USER_IS_BLOCKED.getMessage());
					return response;	
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
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in joinGroup()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse leaveGroup(JoinLeaveGroupRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		try {
			Group group = groupRepository.findOne(request.getGroupId());
			if(group == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}

			GroupMember groupMember = groupMemberRepository.findGroupMember(authenticatedUser.getId(), request.getGroupId());
			if(groupMember == null){
				response.setAcknowledge(true);
				return response;
			}
			
			groupMemberRepository.delete(groupMember);
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in joinGroup()", e);
		}

		return response;
	}
	
	@Override
	public ListGroupResponse getMyGroups(BaseRequest request) {
		ListGroupResponse response = new ListGroupResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		try {
			List<Group> groupList = groupMemberRepository.findGroupsOfUser(authenticatedUser.getId());
			if(groupList == null){
				response.setAcknowledge(true);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}

			for (Group group : groupList) {
				response.addGroup(group.getId(), group.getName(), group.getDescription());
			}
			
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in getMyGroups()", e);
		}

		return response;
	}

	@Override
	public ListGroupResponse getAllGroups(BaseRequest request) {
		ListGroupResponse response = new ListGroupResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			List<Group> groupList = groupRepository.findAll();
			if(groupList == null){
				response.setAcknowledge(true);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}

			for (Group group : groupList) {
				if(group.getStatus() == null || group.getStatus().value() != GroupStatus.ACTIVE.value()){
					continue;
				}
				response.addGroup(group.getId(), group.getName(), group.getDescription());
			}
			
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in getAllGroups()", e);
		}

		return response;
	}
}
