package com.boun.data.mongo.model;

import com.boun.data.Permission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {

    @Id
    private String id;
    
    String name;

    List<Permission> permissions;

    public Role() {
       permissions = new ArrayList<>();
    }
}
