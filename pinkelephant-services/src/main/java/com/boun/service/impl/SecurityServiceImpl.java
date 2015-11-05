package com.boun.service.impl;

import com.boun.app.common.ErrorCode;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.repository.RoleRepository;
import com.boun.http.request.*;
import com.boun.http.response.ActionResponse;
import com.boun.service.PinkElephantService;
import com.boun.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityServiceImpl extends PinkElephantService implements SecurityService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public ActionResponse createRole(CreateRoleRequest request) {

		ActionResponse response = new ActionResponse();

		try {
			Role role = roleRepository.findByName(request.getRole().getName());

			if(role != null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.DUPLICATE_ROLE.getMessage());
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

	public boolean delete(String id) {

		//TODO check if a User has this role, throw exception
		Role role = roleRepository.findById(id);
		roleRepository.delete(role);

		return true;
	}
}
