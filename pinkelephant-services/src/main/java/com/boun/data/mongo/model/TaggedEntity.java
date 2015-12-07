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
	
	public boolean updateTagList(List<String> newList){
		
		if(tagList == null || tagList.isEmpty()){
			
			if(newList == null || newList.isEmpty()){
				return false;
			}
			
			for (String tag : newList) {
				add(tag);	
			}
			return true;
		}
		
		if(newList == null || newList.isEmpty()){
			
			if(tagList == null || tagList.isEmpty()){
				return false;
			}
			
			for (String tag : tagList) {
				remove(tag);	
			}
			return true;
		}
		
		boolean updated = false;
		
		for (String newTag : newList) {
			if(!tagList.contains(newTag)){
				add(newTag);
				updated = true;
			}
		}
		
		for (String newTag : tagList) {
			if(!newList.contains(newTag)){
				remove(newTag);	
				updated = true;
			}
		}
		
		return updated;
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
