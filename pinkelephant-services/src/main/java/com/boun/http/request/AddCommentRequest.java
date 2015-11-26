package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AddCommentRequest extends BaseRequest{

	private String discussionId;
	private String comment;
	
}
