package com.boun.data.mongo.model;

import com.boun.data.common.enums.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Note extends BaseEntity{

    private String title;

    private String text;

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

}
