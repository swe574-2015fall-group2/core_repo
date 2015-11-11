package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.repository.RoleRepository;
import com.boun.http.request.CreateRoleRequest;
import com.boun.http.request.UpdateRoleRequest;
import com.boun.service.PinkElephantService;
import com.boun.service.RoleService;

@Service
public class RoleServiceImpl extends PinkElephantService implements RoleService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public Role findById(String id) {
		Role role = roleRepository.findOne(id);

		if(role == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.ROLE_NOT_FOUND, "todo dev message");
		}

		return role;
	}

	@Override
	public Role createRole(CreateRoleRequest request) {
		Role role = roleRepository.findByName(request.getName());

		if(role != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_ROLE, "todo dev message");
		}
		
		role = new Role();
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
			throw new PinkElephantRuntimeException(400, ErrorCode.SOME_ROLES_NOT_FOUND, "");
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
