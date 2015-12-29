package com.boun.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "entityrelation")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityRelation extends BaseEntity{

	public EntityRelation(){
	}
	
	@DBRef
	private Meeting meeting;

	@DBRef
	private Discussion discussion;
	
	@DBRef
	private Resource resource;
}