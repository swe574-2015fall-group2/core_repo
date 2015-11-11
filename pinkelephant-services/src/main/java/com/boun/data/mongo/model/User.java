package com.boun.data.mongo.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.boun.data.common.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Document(collection = "users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends BaseEntity{

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

    private UserDetail userDetail;

    private UserPreferences preferences;

    private Status status;
    //

    public UserRole getGroupRoles(String groupId) {
        UserRole role = new UserRole();

        if(roles == null) {
            roles = new HashSet<UserRole>();
            role.setGroupId(groupId);
            roles.add(role);
        } else {
            for (UserRole userRole : roles) {
                if (userRole.getGroupId().equals(groupId)) {
                    role = userRole;
                    break;
                }
            }
        }

        return role;
    }

}
