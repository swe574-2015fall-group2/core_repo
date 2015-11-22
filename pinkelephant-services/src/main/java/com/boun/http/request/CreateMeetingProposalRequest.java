package com.boun.http.request;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateMeetingProposalRequest extends BaseRequest{

	private String discussionId;
	
	private String message;
	private Date datetime;
}
