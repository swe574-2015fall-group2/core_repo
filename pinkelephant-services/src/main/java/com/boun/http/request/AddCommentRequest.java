package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AddCommentRequest extends BaseRequest{

	@NotNull
	private String discussionId;
	
	@NotNull
	private String comment;
	
}
