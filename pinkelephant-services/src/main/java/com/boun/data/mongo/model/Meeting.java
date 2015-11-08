package com.boun.data.mongo.model;

import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boun.data.common.MeetingStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Document(collection = "meeting")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meeting {

	@Id
	private String id;

	private Date datetime;
	private Set<String> agendaSet;
	private Integer estimatedDuration;
	private Integer actualDuration;
	private String location;
	private String description;

	private MeetingStatus status;

	@DBRef
	private Group group;

	@DBRef
	private Set<User> invitedUserSet;

	@DBRef
	private Set<User> attendedUserSet;
}
