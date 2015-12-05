package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SendMessageRequest extends BaseRequest{

	private String receiverId;
	private String message;
}
