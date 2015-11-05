package com.boun.http.request;

import com.boun.data.mongo.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SetRolesRequest {

	private String userId;

	private String groupId;

	private Set<String> roleIds;
}
