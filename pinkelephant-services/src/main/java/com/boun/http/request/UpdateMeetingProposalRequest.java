package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateMeetingProposalRequest extends CreateMeetingProposalRequest{

	private String id;
	private boolean status;
}
