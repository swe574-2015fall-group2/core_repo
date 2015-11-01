package com.boun.http.request;

import com.boun.data.mongo.model.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class CreateUserRequest{

	private User user;
}
