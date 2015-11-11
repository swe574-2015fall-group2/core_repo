package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.util.KeyUtils;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserRole;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.AuthenticationRequest;
import com.boun.http.request.ChangePasswordRequest;
import com.boun.http.request.CreateUserRequest;
import com.boun.http.request.ResetPasswordRequest;
import com.boun.http.request.SetRolesRequest;
import com.boun.http.request.UpdateUserRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;
import com.boun.service.GroupService;
import com.boun.service.MailService;
import com.boun.service.PinkElephantService;
import com.boun.service.RoleService;
import com.boun.service.UserService;

@Service
public class UserServiceImpl extends PinkElephantService implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MailService mailService;

	@Autowired
	private RoleService roleService;

	public User findById(String id) {
		User user = userRepository.findOne(id);

		if(user == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
	 	}

		return user;
	}

	@Override
	public ActionResponse createUser(CreateUserRequest request) {

		ActionResponse response = new ActionResponse();
		
		User user = userRepository.findByUsername(request.getUser().getUsername());
		if(user != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_USER, "");
		}

		userRepository.save(request.getUser());
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ActionResponse updateUser(UpdateUserRequest request) {

		validate(request);
		
		//TODO only admins and profile owners should be able to updateUser
		ActionResponse response = new ActionResponse();

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		if(!authenticatedUser.getId().equalsIgnoreCase(request.getId())){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Input userId is different than authenticated user", "");
		}
		
		User user =	findById(request.getId());

		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.getUserDetail().setBirthDate(request.getBirthDate());
		user.getUserDetail().setProfession(request.getProfession());
		user.getUserDetail().setUniversity(request.getUniversity());
		user.getUserDetail().setProgramme(request.getProgramme());
		user.getUserDetail().setInterestedAreas(request.getInterestedAreas());
		user.getUserDetail().setLinkedinProfile(request.getLinkedinProfile());
		user.getUserDetail().setAcademiaProfile(request.getAcademiaProfile());
		user.getUserDetail().setImagePath(request.getImagePath());

		userRepository.save(user);
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public LoginResponse authenticate(AuthenticationRequest request) {

		LoginResponse response = new LoginResponse();

		// TODO password must be stored encrypted
		User user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

		if (user == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
		}

		response.setToken(KeyUtils.currentTimeUUID().toString());

		PinkElephantSession.getInstance().addToken(response.getToken(), user);

		return response;
	}

	@Override
	public ActionResponse resetPassword(final ResetPasswordRequest request) {
		
		ActionResponse response = new ActionResponse();
		
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
		}
		
		final String oneTimeToken = KeyUtils.currentTimeUUID().toString();
		user.setOneTimeToken(oneTimeToken);
		userRepository.save(user);
		
		mailService.sendMail(request.getUsername(), "Password reset request", "You token for password renewal is " + oneTimeToken);
		
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ActionResponse changePassword(ChangePasswordRequest request) {
		
		ActionResponse response = new ActionResponse();
		
		final User user = userRepository.findByOneTimeToken(request.getOneTimeToken());
		if(user == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
		}
		
		user.setOneTimeToken(null);
		user.setPassword(request.getNewPassword());
		
		userRepository.save(user);

		mailService.sendMail(user.getUsername(), "Password change request", "You password is updated successfully");
		
		response.setAcknowledge(true);
		return response;
		
	}

	@Override
	public User setRoles(final SetRolesRequest request) {

		ActionResponse response = new ActionResponse();

		//If user is the authenticated user, we can get it from session instead of getting userId from service.
		//TODO check if user is null
		User user = findById(request.getUserId());

		// check if there's a group
		Group group = groupService.findById(request.getGroupId());

		//TODO check if group is null
		//TODO check if user is in this group

		UserRole groupRoles = user.getGroupRoles(group.getId());

		List<Role> roles = roleService.findAll(request.getRoleIds());
		groupRoles.setGroupRoles(roles);

		user = userRepository.save(user);

		return user;
	}
}
