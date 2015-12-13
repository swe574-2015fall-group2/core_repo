package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MessageReadRequest extends BaseRequest{

	private String messageBoxId;
	private String messageId;
}
