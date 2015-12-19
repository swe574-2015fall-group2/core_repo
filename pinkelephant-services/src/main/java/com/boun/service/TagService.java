package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.Tag;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.http.request.TagData;

public interface TagService {

	public void tag(TagData tag, TaggedEntity baseEntity, boolean add);
	
	public void tag(List<TagData> tagList, TaggedEntity baseEntity, boolean add);
	
	public List<TaggedEntity> findTaggedEntityList(TagData tagStr, EntityType type);
	
	public List<Tag> findAllTagList();
}
