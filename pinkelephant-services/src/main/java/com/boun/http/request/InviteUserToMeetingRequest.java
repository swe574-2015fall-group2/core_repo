package com.boun.http.request;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InviteUserToMeetingRequest extends BaseRequest{

	private String meetingId;
	private List<String> usernameList;
}
