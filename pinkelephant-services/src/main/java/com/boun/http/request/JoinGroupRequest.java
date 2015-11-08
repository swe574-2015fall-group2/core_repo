package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class JoinGroupRequest extends BaseRequest{

	private String username;
	private String groupId;
}
