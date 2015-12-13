package com.boun.data.cache;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.boun.data.mongo.model.Tag;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.service.TagService;

import lombok.Data;

public class TagCache {

	private static TagCache instance = null;
	
	private Hashtable<String, List<TaggedEntityMetaData>> tagTable = new Hashtable<String, List<TaggedEntityMetaData>>();
	
	private TagCache(){
	}
	
	public static synchronized TagCache getInstance(TagService tagService) {
		if(instance == null){
			instance = new TagCache();
			instance.buildCache(tagService);
		}
		return instance;
	}
	
	private void buildCache(TagService tagService){
		List<Tag> tagList = tagService.findAllTagList();
		
		for (Tag tag : tagList) {
			
			List<TaggedEntityMetaData> list = tagTable.get(tag.getTag());
			if(list == null){
				list = new ArrayList<TaggedEntityMetaData>();
			}
			for (TaggedEntity entity : tag.getReferenceSet()) {
				list.add(new TaggedEntityMetaData(entity.getId(), entity.getEntityType()));
			}
			tagTable.put(tag.getTag(), list);
		}
	}
	
	public List<TaggedEntityMetaData> getTag(String tag){
		return tagTable.get(tag);
	}
	
	public List<String> getAllTags(){
		
		if(tagTable.isEmpty()){
			return null;
		}
		
		return new ArrayList<>(tagTable.keySet());
	}

	public void updateTag(String tag, List<TaggedEntity> tagList){
		List<TaggedEntityMetaData> list = new ArrayList<TaggedEntityMetaData>();
		for (TaggedEntity taggedEntity : tagList) {
			list.add(new TaggedEntityMetaData(taggedEntity.getId(), taggedEntity.getEntityType()));
		}
		tagTable.put(tag, list);
	}
	
	@Data
	public static class TaggedEntityMetaData{
		private String id;
		private TaggedEntity.EntityType type;
		
		private TaggedEntityMetaData(String id, TaggedEntity.EntityType type){
			this.id = id;
			this.type = type;
		}
	}
}