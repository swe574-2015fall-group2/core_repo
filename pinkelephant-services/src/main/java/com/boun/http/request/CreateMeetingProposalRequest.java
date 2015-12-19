package com.boun.http.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateMeetingProposalRequest extends BaseRequest{

	private String discussionId;
	
	@NotNull
	private String message;
	
	@NotNull
	private Date datetime;
}
