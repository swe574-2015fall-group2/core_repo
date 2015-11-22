package com.boun.http.request;

import com.boun.data.mongo.model.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateRoleRequest extends BaseRequest{

	private Role role;
}
