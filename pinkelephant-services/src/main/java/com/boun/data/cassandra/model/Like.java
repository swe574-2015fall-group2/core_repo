package com.boun.data.cassandra.model;

import com.boun.data.mongo.model.UserMetadata;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Transient;
import java.util.Date;

@Data
public class Like {

    private String feedId;

    @JsonIgnore
    private Long userId;

    private Date atCreated;

    @Transient
    private UserMetadata user;

    public Like(){}

    public Like(String feedId, Long userId){
        this.feedId = feedId;
        this.userId = userId;
    }
}
