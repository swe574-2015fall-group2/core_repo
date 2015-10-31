package com.boun.data.mongo.model;

import com.boun.data.common.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    //
    private UserDetail userDetail;

    private UserPreferences preferences;

    private Status status;
    //

}
