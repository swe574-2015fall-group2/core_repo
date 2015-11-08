package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class JoinLeaveGroupRequest extends BaseRequest{

	private String groupId;
}
