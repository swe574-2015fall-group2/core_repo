package com.boun.data.mongo.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Messagebox extends BaseEntity{

	@DBRef
    private User receiver;
	
	@DBRef
    private User sender;
	
	private List<MessageDetails> messages;
	
	public Messagebox(){
	}
	
	public void addMessage(String message){
		if(messages == null || messages.isEmpty()){
			messages = new ArrayList<MessageDetails>();
		}
		messages.add(new MessageDetails(message, String.valueOf(messages.size()+1)));
	}
	
	@Data
	public static class MessageDetails{
		
	    private String id;
		private Date datetime;
		private String message;
		private boolean read;
		
		public MessageDetails(String message, String id){
			this.id = id;
			this.message = message;
			this.datetime = new Date();
			this.read = false;
		}
	}
}
