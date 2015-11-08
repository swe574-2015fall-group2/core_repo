package com.boun.http.request;

import com.boun.data.Permission;
import lombok.Data;
import java.util.List;

@Data
public class CreateRoleRequest extends BaseRequest{

	String name;

	List<Permission> permissions;
}
