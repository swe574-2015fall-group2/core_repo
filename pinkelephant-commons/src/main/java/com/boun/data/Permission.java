package com.boun.data;


import com.fasterxml.jackson.annotation.JsonValue;

public enum Permission {

    CREATE_DISCUSSION("create_discussion"),
    UPDATE_DISCUSSION("update_discussion"),
    COMMENT_TO_DISCUSSION("comment_to_discussion"),
    ADD_REMOVE_TAG_TO_DISCUSSION("add_remove_tag_to_discussion"),
    CREATE_MEETING("create_meeting"),
    UPDATE_MEETING("update_meeting"),
    INVITE_USER_TO_MEETING("invite_user_to_meeting"),
    CREATE_MEETING_PROPOSAL("create_meeting_proposal"),
    UPDATE_MEETING_PROPOSAL("update_meeting_proposal"),
    DELETE_MEETING_PROPOSAL("delete_meeting_proposal"),
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
