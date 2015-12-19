package com.boun.http.request;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;

import com.boun.data.common.enums.MeetingType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateMeetingRequest extends BaseRequest{

	@NotNull
	private String name;
	
	@NotNull
	private Date datetime;
	
	@NotNull
	private String timezone;
	
	@NotNull
	private String startHour;
	
	@NotNull
	private String endHour;
	
	private Set<String> agendaSet;
	
	private String location;
	
	@NotNull
	private String description;
	
	@NotNull
	private MeetingType type;
	
	private Boolean isPinned;
	
	@NotNull
	private String groupId;
	
	private List<String> invitedUserIdList;
	private List<TagData> tagList;
}
