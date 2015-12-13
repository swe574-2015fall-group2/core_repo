package com.boun.data.cache;

import java.util.Hashtable;
import java.util.List;

import com.boun.data.mongo.model.Tag;
import com.boun.service.TagService;

public class TagCache {

	private static TagCache instance = null;
	
	private Hashtable<String, Tag> tagTable = new Hashtable<String, Tag>();
	
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
			tagTable.put(tag.getTag(), tag);
		}
	}
	
	public Tag getTag(String tag){
		return tagTable.get(tag);
	}
}
