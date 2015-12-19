package com.boun.http.request;

import java.util.Set;

import javax.validation.constraints.NotNull;

import com.boun.data.common.enums.MeetingStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateMeetingRequest extends CreateMeetingRequest{

	private Integer actualDuration;
	
	@NotNull
	private String meetingId;
	private Set<String> todoSet;	
	private MeetingStatus status;
}
