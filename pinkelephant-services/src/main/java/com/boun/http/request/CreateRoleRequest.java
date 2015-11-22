package com.boun.http.request;

import com.boun.data.Permission;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateRoleRequest extends BaseRequest{

	String name;

	List<Permission> permissions;
}
