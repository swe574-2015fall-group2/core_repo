package com.boun.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.data.mongo.model.BaseEntity;
import com.boun.data.mongo.model.Tag;
import com.boun.data.mongo.repository.TagRepository;
import com.boun.service.PinkElephantService;
import com.boun.service.TagService;

@Service
public class TagServiceImpl extends PinkElephantService implements TagService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TagRepository tagRepository;
	
	@Override
	public void tag(String tagStr, BaseEntity baseEntity) {
		
		Tag tag = tagRepository.findOne(tagStr);
		if(tag == null){
			tag = new Tag();
			tag.setTag(tagStr);
		}
		
		Set<BaseEntity> referenceSet = tag.getReferenceSet();
		if(referenceSet == null || referenceSet.isEmpty()){
			referenceSet = new HashSet<BaseEntity>();
		}
		
		if(!referenceSet.contains(baseEntity)){
			referenceSet.add(baseEntity);
		}
		
		tag.setReferenceSet(referenceSet);
		
		tagRepository.save(tag);
	}

}
