package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MeetingProposalInvitationReplyRequest extends BaseRequest{

	private String meetingProposalId;
	private boolean reponse;
}
