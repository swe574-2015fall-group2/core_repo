package com.boun.data.mongo.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class GroupCount {

    @DBRef
    Group group;

    long total;


    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

}