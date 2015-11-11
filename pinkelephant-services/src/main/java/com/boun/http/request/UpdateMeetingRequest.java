package com.boun.http.request;

import java.util.Date;
import java.util.Set;

import com.boun.data.common.enums.MeetingStatus;
import com.boun.data.common.enums.MeetingType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateMeetingRequest extends BaseRequest{

	private String meetingId;
	
	private Date datetime;
	private Set<String> agendaSet;
	private Set<String> todoSet;
	private Integer estimatedDuration;
	private Integer actualDuration;
	private String location;
	private String description;
	
	private MeetingStatus status;
	private MeetingType type;
}
