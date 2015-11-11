package com.boun.http.request;

import com.boun.data.common.enums.MeetingInvitationResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MeetingInvitationReplyRequest extends BaseRequest{

	private String meetingId;
	private MeetingInvitationResult result;
}
