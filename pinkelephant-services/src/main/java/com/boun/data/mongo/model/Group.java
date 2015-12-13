package com.boun.data.mongo.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.boun.data.common.enums.GroupStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends TaggedEntity{

	public Group(){
		super(EntityType.GROUP);
	}
	
    private String name;
    
    private GroupStatus status;
    
    private ImageInfo image;

    private Date createdAt;
    
    @DBRef
    private User creator;
    
}
