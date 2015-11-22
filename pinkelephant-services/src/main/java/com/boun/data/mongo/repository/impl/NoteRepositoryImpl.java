package com.boun.data.mongo.repository.impl;

import com.boun.data.mongo.model.Note;
import com.boun.data.mongo.repository.custom.NoteRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class NoteRepositoryImpl implements NoteRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public List<Note> findNotes(String groupId) {

		Query query = new Query();
		query.addCriteria(Criteria.where("group.$id").is(new ObjectId(groupId)));

		List<Note> noteList = mongoTemplate.find(query, Note.class);

		return noteList;
	}
}
