package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessageReadRequest extends BaseRequest{

	@NotNull
	private String messageBoxId;
	
	@NotNull
	private String messageId;
}
