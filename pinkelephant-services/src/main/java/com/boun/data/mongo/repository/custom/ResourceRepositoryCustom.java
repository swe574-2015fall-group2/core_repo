package com.boun.data.mongo.repository.custom;

import com.boun.data.mongo.model.Resource;

import java.util.List;

public interface ResourceRepositoryCustom {

	List<Resource> findResources(String groupId);
}
