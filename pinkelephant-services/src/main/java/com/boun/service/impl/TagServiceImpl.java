package com.boun.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.data.mongo.model.Tag;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.data.mongo.repository.TagRepository;
import com.boun.service.PinkElephantService;
import com.boun.service.TagService;

@Service
public class TagServiceImpl extends PinkElephantService implements TagService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TagRepository tagRepository;
	
	@Override
	public void tag(String tagStr, TaggedEntity baseEntity, boolean add) {
		
		Tag tag = tagRepository.findOne(tagStr);
		if(tag == null){
			
			if(!add){
				return;
			}
			
			tag = new Tag();
			tag.setTag(tagStr);
		}
		
		List<TaggedEntity> referenceSet = tag.getReferenceSet();
		if(referenceSet == null || referenceSet.isEmpty()){
			
			if(!add){
				return;
			}
			
			referenceSet = new ArrayList<TaggedEntity>();
		}
		
		if(add){
		
			if(!referenceSet.contains(baseEntity)){
				referenceSet.add(baseEntity);
			}
			
		}else{
			
			if(referenceSet.contains(baseEntity)){
				referenceSet.remove(baseEntity);
			}
		}
		
		tag.setReferenceSet(referenceSet);
		
		tagRepository.save(tag);
	}

	@Override
	public void tag(List<String> tagList, TaggedEntity baseEntity, boolean add) {
		if(tagList == null || tagList.isEmpty()){
			return;
		}
		for (String tag : tagList) {
			tag(tag, baseEntity, add);
		}
	}
	
	public List<TaggedEntity> findTaggedEntityList(String tagStr, EntityType type){
		Tag tag = tagRepository.findOne(tagStr);
		
		if(tag == null){
			return null;
		}
		
		List<TaggedEntity> resultList = new ArrayList<TaggedEntity>();
		for (TaggedEntity entity : tag.getReferenceSet()) {
			if(entity.getEntityType() == type){
				resultList.add(entity);
			}
		}
		return resultList;
	}

}
