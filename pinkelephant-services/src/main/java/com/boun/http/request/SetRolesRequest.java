package com.boun.http.request;

import java.util.Set;

import lombok.Data;

@Data
public class SetRolesRequest {

	private String userId;

	private String groupId;

	private Set<String> roleIds;
}
