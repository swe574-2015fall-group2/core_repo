package com.boun.http.request;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InviteUserToMeetingRequest extends BaseRequest{

	@NotNull
	private String meetingId;
	
	@NotNull
	@Min(value=1)
	private List<String> userIdList;
}
