package com.boun.service.impl;

import java.util.List;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Group;
import com.boun.http.request.*;
import com.boun.service.GroupService;
import com.boun.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.util.KeyUtils;
import com.boun.data.common.pool.PinkElephantThreadPool;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserRole;
import com.boun.data.mongo.repository.RoleRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.data.util.MailSender;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;
import com.boun.service.PinkElephantService;
import com.boun.service.UserService;

@Service
public class UserServiceImpl extends PinkElephantService implements UserService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private RoleService roleService;

	public User findById(String id) {
		User user = userRepository.findOne(id);

		if(user == null) {
			throw new PinkElephantRuntimeException(400, "couldn't find user", "", "");
	 	}

		return user;
	}

	@Override
	public ActionResponse createUser(CreateUserRequest request) {

		ActionResponse response = new ActionResponse();

//		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
//			response.setAcknowledge(false);
//			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
//			return response;
//		}

		try {
			User user = userRepository.findByUsername(request.getUser().getUsername());
			if(user != null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.DUPLICATE_USER.getMessage());
				return response;
			}

			userRepository.save(request.getUser());
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createUser()", e);
		}

		return response;
	}

	@Override
	public ActionResponse updateUser(UpdateUserRequest request) {

		//TODO only admins and profile owners should be able to updateUser
		ActionResponse response = new ActionResponse();

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

		try {
			userRepository.save(user);
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createUser()", e);
		}

		return response;
	}

	@Override
	public LoginResponse authenticate(AuthenticationRequest request) {

		LoginResponse response = new LoginResponse();
		try {
			User user = userRepository.findByUsernameAndPassword(request.getUsername(), request.getPassword());

			if (user == null) {
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.USER_NOT_FOUND.getMessage());
				return response;
			}

			response.setAcknowledge(true);
			response.setToken(KeyUtils.currentTimeUUID().toString());

			PinkElephantSession.getInstance().addToken(response.getToken(), user);

		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in authenticate()", e);
		}

		return response;
	}

	@Override
	public ActionResponse resetPassword(final ResetPasswordRequest request) {
		
		ActionResponse response = new ActionResponse();
		try{
			User user = userRepository.findByUsername(request.getUsername());
			if(user == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.USER_NOT_FOUND.getMessage());
				return response;
			}
			
			final String oneTimeToken = KeyUtils.currentTimeUUID().toString();
			user.setOneTimeToken(oneTimeToken);
			userRepository.save(user);
			
			PinkElephantThreadPool.EMAIL_POOL.runTask(new Runnable() {

				@Override
				public void run() {
					MailSender.getInstance().sendMail(request.getUsername(), "Password reset request", "You token for password renewal is " + oneTimeToken);
				}
			});
			
			
			response.setAcknowledge(true);
		}catch(Throwable e){
			
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());
			
			logger.error("Error in resetPassword()", e);
		}
		return response;
	}

	@Override
	public ActionResponse changePassword(ChangePasswordRequest request) {
		
		ActionResponse response = new ActionResponse();
		try{
			final User user = userRepository.findByOneTimeToken(request.getOneTimeToken());
			if(user == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.USER_NOT_FOUND.getMessage());
				return response;
			}
			
			user.setOneTimeToken(null);
			user.setPassword(request.getNewPassword());
			
			userRepository.save(user);


			//TODO move this to its own service if necessary
			PinkElephantThreadPool.EMAIL_POOL.runTask(new Runnable() {
				
				@Override
				public void run() {
					MailSender.getInstance().sendMail(user.getUsername(), "Password change request", "You password is updated successfully");					
				}
			});
			
			
			response.setAcknowledge(true);
		}catch(Throwable e){
			
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());
			
			logger.error("Error in changePassword()", e);
		}
		return response;
		
	}

	@Override
	public ActionResponse setRoles(final SetRolesRequest request) {

		ActionResponse response = new ActionResponse();
		try{
			User user = findById(request.getUserId());

			// check if there's a group
			Group group = groupService.findById(request.getGroupId());

			//TODO check if user is in this group

			UserRole groupRoles = user.getGroupRoles(group.getId());

			List<Role> roles = roleService.findAll(request.getRoleIds());
			groupRoles.setGroupRoles(roles);

			userRepository.save(user);

			response.setAcknowledge(true);
		}catch(Throwable e){

			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in resetPassword()", e);
		}
		return response;
	}
}
