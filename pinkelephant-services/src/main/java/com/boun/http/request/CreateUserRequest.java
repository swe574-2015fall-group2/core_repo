package com.boun.http.request;

import com.boun.data.mongo.model.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateUserRequest extends BaseRequest{

	private User user;
}
