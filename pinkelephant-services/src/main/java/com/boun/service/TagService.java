package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.BaseEntity;

public interface TagService {

	public void tag(String tag, BaseEntity baseEntity, boolean add);
	
	public void tag(List<String> tagList, BaseEntity baseEntity, boolean add);
}
