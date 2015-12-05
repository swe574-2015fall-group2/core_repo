package com.boun.data.mongo.repository.custom;

import com.boun.data.mongo.model.Group;

import java.util.List;

public interface GroupRepositoryCustom {

    List<Group> findLatestGroups();

}
