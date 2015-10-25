package com.boun.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "user_metadata")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMetadata  {

    @Id
    private String id;

    private String username;



    public static UserMetadata from(User user) {
        UserMetadata userMetadata = new UserMetadata();
        userMetadata.setId(user.getId());
        userMetadata.setUsername(user.getUsername());


        return userMetadata;
    }

    public UserMetadata simplify() {
        return this;
    }

}
