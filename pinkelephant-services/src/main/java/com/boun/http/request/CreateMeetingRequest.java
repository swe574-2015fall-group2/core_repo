package com.boun.http.request;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.boun.data.common.enums.MeetingType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateMeetingRequest extends BaseRequest{

	private String name;
	private Date datetime;
	private String timezone;
	private String startHour;
	private String endHour;
	private Set<String> agendaSet;
	private String location;
	private String description;
	private MeetingType type;
	private Boolean isPinned;
	
	private String groupId;
	
	private List<String> invitedUserIdList;
	private List<TagData> tagList;
}
