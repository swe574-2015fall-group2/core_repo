package com.boun.service.impl;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.repository.RoleRepository;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.service.PinkElephantService;
import com.boun.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends PinkElephantService implements RoleService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public ActionResponse createRole(CreateRoleRequest request) {

		ActionResponse response = new ActionResponse();

		try {
			Role role = roleRepository.findByName(request.getName());

			if(role != null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.DUPLICATE_ROLE.getMessage());
				return response;
			}

			role.setName(request.getName());
			role.setPermissions(request.getPermissions());

			roleRepository.save(role);
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createRole()", e);
		}

		return response;
	}

	@Override
	public ActionResponse updateRole(UpdateRoleRequest request) {

		ActionResponse response = new ActionResponse();

		try {
			Role role = roleRepository.findOne(request.getRole().getId());

			if(role == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.ROLE_NOT_FOUND.getMessage());
				return response;
			}

			roleRepository.save(request.getRole());
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createRole()", e);
		}

		return response;
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public List<Role> findAll(List<String> ids) {
		List<Role> roles = (List<Role>)roleRepository.findAll(ids);

		if(roles.size() != ids.size()) {
			throw new PinkElephantRuntimeException(400, "400", "", "");
		}

		return roles;
	}

	public boolean delete(String id) {

		//TODO check if a User has this role, throw exception
		Role role = roleRepository.findOne(id);
		roleRepository.delete(role);

		return true;
	}
}
