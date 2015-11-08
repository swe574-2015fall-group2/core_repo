package com.boun.service.impl;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.http.request.UpdateGroupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.repository.GroupRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateGroupRequest;
import com.boun.http.response.ActionResponse;
import com.boun.service.GroupService;
import com.boun.service.PinkElephantService;

@Service
public class GroupServiceImpl extends PinkElephantService implements GroupService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GroupRepository groupRepository;

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
}
