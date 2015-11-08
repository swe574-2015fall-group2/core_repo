package com.boun.http.request;

import java.util.Date;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateMeetingRequest extends BaseRequest{

	private Date datetime;
	private Set<String> agendaSet;
	private Integer estimatedDuration;
	private String location;
	private String description;
	
	private String groupName;
}
