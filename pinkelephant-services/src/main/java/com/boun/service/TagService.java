package com.boun.service;

import com.boun.data.mongo.model.BaseEntity;

public interface TagService {

	public void tag(String tag, BaseEntity baseEntity);
}
