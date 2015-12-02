package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.util.KeyUtils;
import com.boun.data.common.enums.Status;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.ImageInfo;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserDetail;
import com.boun.data.mongo.model.UserRole;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.AuthenticationRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.BasicSearchRequest;
import com.boun.http.request.ChangePasswordRequest;
import com.boun.http.request.CreateUserRequest;
import com.boun.http.request.ResetPasswordRequest;
import com.boun.http.request.SetRolesRequest;
import com.boun.http.request.UpdateUserRequest;
import com.boun.http.request.UploadImageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetUserResponse;
import com.boun.http.response.LoginResponse;
import com.boun.http.response.SearchUserResponse;
import com.boun.service.GroupService;
import com.boun.service.MailService;
import com.boun.service.PinkElephantService;
import com.boun.service.RoleService;
import com.boun.service.UserService;
import com.boun.util.ImageUtil;

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
	public User createUser(CreateUserRequest request) {

		User user = userRepository.findByUsername(request.getUsername());
		if(user != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_USER, "");
		}

		user = userRepository.save(mapUser(request));

		return user;
	}

	@Override
	public GetUserResponse getUser(BasicQueryRequest request){
		
		validate(request);
		
		User user = findById(request.getId());
		
		GetUserResponse response = new GetUserResponse();
		response.setUsername(user.getUsername());
		response.setFirstname(user.getFirstname());
		response.setLastname(user.getLastname());
		response.setStatus(user.getStatus());
		response.setUserDetail(user.getUserDetail());
		response.setImage(ImageUtil.getImage(user.getImage()));
		response.setAcknowledge(true);
		
		return response;
	}
	
	@Override
	public SearchUserResponse searchUser(BasicSearchRequest request){
		
		validate(request);
		
		List<User> userlist = userRepository.searchUser(request.getQueryString());
		if(userlist == null || userlist.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
		}
		
		SearchUserResponse response = new SearchUserResponse();
		for (User user : userlist) {
			response.addUser(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname());
		}
		
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
		response.setId(user.getId());
		
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
	
	@Override
	public ActionResponse uploadImage(UploadImageRequest request){
		
		validate(request);
		
		User user = findById(request.getEntityId());
		
		String imagePath = ImageUtil.saveImage("Group", request);

		ImageInfo image = new ImageInfo();
		image.setImagePath(imagePath);
		image.setType(request.getFileType());
		
		user.setImage(image);
		userRepository.save(user);
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}
	
	private User mapUser(CreateUserRequest request){
		User user = new User();
		
		user.setFirstname(request.getFirstname());
		user.setLastname(request.getLastname());
		user.setPassword(request.getPassword());
		user.setStatus(Status.ACTIVE);
		user.setUsername(request.getUsername());
		
		UserDetail detail = new UserDetail();
		detail.setAcademiaProfile(request.getAcademiaProfile());
		detail.setBirthDate(request.getBirthDate());
		detail.setInterestedAreas(request.getInterestedAreas());
		detail.setLinkedinProfile(request.getLinkedinProfile());
		detail.setProfession(request.getProfession());
		detail.setProgramme(request.getProgramme());
		detail.setUniversity(request.getUniversity());
		
		user.setUserDetail(detail);
		
		return user;
		
	}
}
