package com.boun.data.mongo.model;

import com.boun.app.exception.PinkElephantException;
import com.boun.app.exception.PinkElephantRuntimeException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.boun.data.common.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

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

    private Set<UserRole> roles;

    //
    private UserDetail userDetail;

    private UserPreferences preferences;

    private Status status;
    //

    public UserRole getGroupRoles(String groupId) {
        if(roles == null) {
            roles = new HashSet<UserRole>();

            UserRole role = new UserRole();
            role.setGroupId(groupId);

            roles.add(role);

            return role;
        } else {
            for (UserRole userRole : roles) {
                if (userRole.getGroupId().equals(groupId))
                    return userRole;
            }
        }

        throw new PinkElephantRuntimeException(400, "couldn't get group roles", "", "");
    }

}
