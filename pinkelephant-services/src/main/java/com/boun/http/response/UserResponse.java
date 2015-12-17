package com.boun.http.response;

import com.boun.data.common.enums.Status;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserDetail;
import com.boun.data.mongo.model.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

	private String username;
	private String firstname;
	private String lastname;
	private UserDetail userDetail;
	private ImageData image;
	private Status status;
	private Set<UserRole> roles;

	public static List<UserResponse> userToUserResponse(List<User> userList) {
		List<UserResponse> userResponses = new ArrayList<UserResponse>();
		if(userList == null || userList.size() == 0)
			return userResponses;

		for(User user : userList) {
			UserResponse response = new UserResponse();

			response.setRoles(user.getRoles());
			response.setFirstname(user.getFirstname());
			response.setLastname(user.getLastname());
			response.setStatus(user.getStatus());
			response.setUserDetail(user.getUserDetail());
			response.setUsername(user.getUsername());

			userResponses.add(response);
		}

		return userResponses;
	}
}
