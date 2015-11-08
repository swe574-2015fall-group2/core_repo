package com.boun.data;


import com.fasterxml.jackson.annotation.JsonValue;

public enum Permission {

    ADD_FEED("add_feed"),
    REMOVE_FEED("remove_feed"),
    CREATE_MEETING("create_meeting"),
    DELETE_MEETING("delete_meeting"),
    CREATE_GROUP("create_group"),
    DELETE_GROUP("remove_feed");

    private String value;

    private Permission(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
