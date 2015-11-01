package com.boun.data.mongo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.boun.data.common.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    private String id;

    @Field("username")
    private String username;

    @Field("firstname")
    private String firstname;

    @Field("lastname")
    private String lastname;

    @Field("password")
    private String password;

    @JsonIgnore
    @Field("oneTimeToken")
    private String oneTimeToken;
    
    //
    private UserDetail userDetail;

    private UserPreferences preferences;

    private Status status;
    //

}
