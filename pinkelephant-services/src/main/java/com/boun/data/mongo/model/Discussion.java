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
@Document(collection = "discussion")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Discussion extends TaggedEntity{

	public Discussion(){
		super(EntityType.DISCUSSION);
	}
	
	@DBRef
	private Group group;
	
	@DBRef
	private User creator;

	@DBRef
	private List<Resource> resources;

	private String name;
	private Date creationTime;
	private Date updateTime;
	private Boolean isPinned;
}
