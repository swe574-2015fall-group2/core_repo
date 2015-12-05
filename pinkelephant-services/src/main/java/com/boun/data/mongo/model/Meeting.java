package com.boun.data.mongo.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boun.data.common.enums.MeetingStatus;
import com.boun.data.common.enums.MeetingType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "meeting")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meeting extends TaggedEntity{

	private String name;
	private Date datetime;
	private String timezone;
	private Set<String> agendaSet;
	private Set<String> todoSet;
	private String startHour;
	private String endHour;
	private Integer actualDuration;
	private String location;
	private String description;

	private MeetingStatus status;
	private MeetingType type;

	public Meeting(){
		super(EntityType.MEETING);
	}
	
	@DBRef
	private User creator;
	
	@DBRef
	private Group group;

	@DBRef
	private Set<User> invitedUserSet;

	@DBRef
	private Set<User> attendedUserSet;
	
	@DBRef
	private Set<User> rejectedUserSet;
	
	@DBRef
	private Set<User> tentativeUserSet;
}
