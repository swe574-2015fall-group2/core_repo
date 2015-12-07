package com.boun.data.mongo.model;

import java.util.ArrayList;
import java.util.List;

import com.boun.data.Permission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseEntity{

    private String name;

    private List<Permission> permissions;

    public Role() {
       permissions = new ArrayList<>();
    }
}
