package com.boun.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.MemberStatus;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.GroupMemberRepository;
import com.boun.data.mongo.repository.GroupRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateGroupRequest;
import com.boun.http.request.JoinGroupRequest;
import com.boun.http.request.UpdateGroupRequest;
import com.boun.http.response.ActionResponse;
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
	public ActionResponse createGroup(CreateGroupRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			Group group = groupRepository.findByGroupName(request.getName());
			if(group != null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.DUPLICATE_GROUP.getMessage());
				return response;
			}

			group = new Group();
			group.setName(request.getName());
			group.setDescription(request.getDescription());

			groupRepository.save(group);
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createGroup()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse updateGroup(UpdateGroupRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			Group group = groupRepository.findOne(request.getGroup().getId());
			if(group == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}

			request.getGroup().setId(group.getId());
			groupRepository.save(request.getGroup());
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in updateGroup()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse joinGroup(JoinGroupRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		if(!authenticatedUser.getUsername().equalsIgnoreCase(request.getUsername())){
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.AUTHTOKEN_AND_AUTHENTICATED_USER_NOT_MATCH.getMessage());
			return response;
		}

		try {
			Group group = groupRepository.findOne(request.getGroupId());
			if(group == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}

			User user = userRepository.findByUsername(request.getUsername());
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
}
