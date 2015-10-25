package com.boun.data.cassandra.model;

import com.boun.data.mongo.model.UserMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.UUID;

@Data
public class Comment {

    private String id;

    @JsonIgnore
    private Long userId;

    @Indexed
    private String feedId;

    private String text;

    private Date atCreated;

    @JsonIgnore
    private UUID ts;

    @Transient
    private UserMetadata user;
}
