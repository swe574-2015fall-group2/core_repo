package com.boun.data.mongo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserPreferences {


    @JsonIgnore
    private List<Long> blockedUserIds;

    @Transient
    private Set<UserMetadata> blockedUsers;

    public UserPreferences() {

        this.blockedUserIds = new ArrayList<Long>();
    }
}
