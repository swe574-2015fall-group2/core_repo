package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateDiscussionRequest extends BaseRequest{

	private String groupId;
	private String name;
	private String description;
	
}
