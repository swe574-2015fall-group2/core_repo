package com.boun.data.mongo.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaggedEntity extends BaseEntity{

	private List<String> tagList;
	
	private String description;
	
    @Transient
    private EntityType entityType;
    
	public TaggedEntity(EntityType type) {
		this.entityType = type;
	}

    public enum EntityType{
    	DISCUSSION, GROUP, MEETING, NOTE, RESOURCE;
    }
    
	protected void overwriteTagList(List<String> tagList){
		this.tagList = tagList;
	}
	
	public boolean updateTagList(String tag, boolean add){
		if(add){
			return add(tag);
		}
		
		return remove(tag);
	}
	
	public List<String> removeTagList(List<String> newList){
		
		if(tagList == null || tagList.isEmpty()){
			return null;
		}
		
		if(newList == null || newList.isEmpty()){
			List<String> copyTagList = new ArrayList<>(tagList);
			tagList = null;
			return copyTagList;
		}
		
		List<String> removeList = new ArrayList<String>();
		
		List<String> copyTagList = new ArrayList<>(tagList);
		for (String tag : copyTagList) {
			if(!newList.contains(tag)){
				removeList.add(tag);
			}
		}
		tagList = newList;
		
		return removeList;
	}
	
	public List<String> addTagList(List<String> newList){
		
		if(newList == null || newList.isEmpty()){
			return null;
		}
		
		if(tagList == null || tagList.isEmpty()){
			tagList = newList;
			return tagList;
		}

		List<String> addedList = new ArrayList<String>();
		
		for (String newTag : newList) {
			if(!tagList.contains(newTag)){
				add(newTag);
				addedList.add(newTag);
			}
		}
		return addedList;
	}
	
	private boolean remove(String tag){
		
		if(tag == null || "".equalsIgnoreCase(tag)){
			return false;
		}
		
		if(tagList == null || tagList.isEmpty()){
			return false;
		}
		
		return tagList.remove(tag);
	}
	
	private boolean add(String tag){
		
		if(tag == null || "".equalsIgnoreCase(tag)){
			return false;
		}
		
		if(tagList == null || tagList.isEmpty()){
			tagList = new ArrayList<String>();
		}
		
		if(!tagList.contains(tag)){
			tagList.add(tag);
			return true;
		}
		
		return false;
	}
}
