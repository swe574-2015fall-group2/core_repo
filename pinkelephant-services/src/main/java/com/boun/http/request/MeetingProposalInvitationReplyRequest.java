package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MeetingProposalInvitationReplyRequest extends BaseRequest{

	@NotNull
	private String meetingProposalId;
	
	@NotNull
	private boolean reponse;
}
