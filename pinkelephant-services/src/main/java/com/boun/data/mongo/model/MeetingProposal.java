package com.boun.data.mongo.model;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "meeting")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MeetingProposal extends BaseEntity{

	private String message;
	private Date datetime;
	private boolean status;
	
	@DBRef
	private User creator;
	
	@DBRef
	private Discussion discussion;
	
	private List<Respondant> respondantList;
	
	public MeetingProposal(){
		super(EntityType.MEETING_PROPOSAL);
	}
	
	@Data
	public static class Respondant{
		
		@DBRef
		private User user;
		private boolean response;
		
		public Respondant(User user, boolean response){
			this.user = user;
			this.response = response;
		}
	}
}
