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
	public Role findById(String id) {
		Role role = roleRepository.findOne(id);

		if(role == null) {
			throw new PinkElephantRuntimeException(400, "400", ErrorCode.ROLE_NOT_FOUND.getMessage(), "todo dev message");
		}

		return role;
	}

	@Override
	public Role createRole(CreateRoleRequest request) {
		Role role = roleRepository.findByName(request.getName());

		if(role != null){
			throw new PinkElephantRuntimeException(400, "400", ErrorCode.DUPLICATE_ROLE.getMessage(), "todo dev message");
		}

		role.setName(request.getName());
		role.setPermissions(request.getPermissions());
		role = roleRepository.save(role);

		return role;
	}

	@Override
	public Role updateRole(UpdateRoleRequest request) {

		Role role = findById(request.getRole().getId());
		role = roleRepository.save(request.getRole());

		return role;
	}

	public List<Role> findAll() {
		return roleRepository.findAll();
	}

	public List<Role> findAll(List<String> ids) {
		List<Role> roles = (List<Role>)roleRepository.findAll(ids);

		if(roles.size() != ids.size()) {
			throw new PinkElephantRuntimeException(400, "400", "some roles couldn't be found", "");
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
