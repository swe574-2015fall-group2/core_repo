package com.boun.data.mongo.model;

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
public class Note extends TaggedEntity{

    private String title;

    @DBRef
    private List<Resource> resources;

    @DBRef
    private Group group;

    @DBRef
    private Meeting meeting;

    private Date createdAt;

    //TODO UserMetadata must be sent to frontend instead of User (security reasons:password)
    @DBRef
    private User creator;

	public Note(){
		super(EntityType.NOTE);
	}
}
