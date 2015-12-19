package com.boun.http.request;

import javax.validation.constraints.NotNull;

import com.boun.data.common.enums.MeetingInvitationResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MeetingInvitationReplyRequest extends BaseRequest{

	@NotNull
	private String meetingId;
	
	@NotNull
	private MeetingInvitationResult result;
}
