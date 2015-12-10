package com.boun.http.response;

import com.boun.data.common.enums.Status;
import com.boun.data.mongo.model.UserDetail;
import com.boun.data.mongo.model.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetUserResponse extends ActionResponse {

	private String username;
	private String firstname;
	private String lastname;
	private UserDetail userDetail;
	private ImageData image;
	private Status status;
	private Set<UserRole> roles;

}
