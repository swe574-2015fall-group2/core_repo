package com.boun.data.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseEntity {
	
	protected BaseEntity(EntityType entityType){
		this.entityType = entityType;
	}
	
    @Id
    private String id;
    
    @Transient
    private EntityType entityType;
    
    public boolean isEqual(BaseEntity request){
    	return this.id.equalsIgnoreCase(request.getId());
    }

    public enum EntityType{
    	COMMENT, DISCUSSION, GROUP, GROUPMEMBER, MEETING, MEETING_PROPOSAL, NOTE, RESOURCE, ROLE, MESSAGEBOX, USER
    }
}
