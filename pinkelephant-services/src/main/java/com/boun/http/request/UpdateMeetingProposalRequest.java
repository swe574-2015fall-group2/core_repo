package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateMeetingProposalRequest extends CreateMeetingProposalRequest{

	@NotNull
	private String id;
	
	@NotNull
	private boolean status;
}
