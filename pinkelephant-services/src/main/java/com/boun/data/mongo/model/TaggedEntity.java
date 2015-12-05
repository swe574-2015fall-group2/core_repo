package com.boun.data.mongo.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaggedEntity extends BaseEntity{

	private List<String> tagList;
	
	public TaggedEntity(EntityType type) {
		super(type);
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
