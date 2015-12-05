package com.boun.data.mongo.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.boun.data.common.enums.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource extends TaggedEntity{

    private String name;

    private String link;

    private ResourceType type;

    @DBRef
    private Group group;

    private Date createdAt;

    //TODO UserMetadata must be sent to frontend instead of User (security reasons:password)
    @DBRef
    private User creator;

	public Resource(){
		super(EntityType.RESOURCE);
	}
}
