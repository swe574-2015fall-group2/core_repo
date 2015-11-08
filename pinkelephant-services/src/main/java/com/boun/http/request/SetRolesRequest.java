package com.boun.http.request;

import java.util.List;
import java.util.Set;

import lombok.Data;

@Data
public class SetRolesRequest extends BaseRequest{

	private String userId;

	private String groupId;

	private List<String> roleIds;
}
