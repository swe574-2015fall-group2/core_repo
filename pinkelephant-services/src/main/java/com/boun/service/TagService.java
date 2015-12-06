package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.BaseEntity.EntityType;

public interface TagService {

	public void tag(String tag, TaggedEntity baseEntity, boolean add);
	
	public void tag(List<String> tagList, TaggedEntity baseEntity, boolean add);
	
	public List<TaggedEntity> findTaggedEntityList(String tagStr, EntityType type);
}
