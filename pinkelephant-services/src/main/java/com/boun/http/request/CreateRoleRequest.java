package com.boun.http.request;

import com.boun.data.mongo.model.Role;
import lombok.Data;

@Data
public class CreateRoleRequest {

	private Role role;
}
