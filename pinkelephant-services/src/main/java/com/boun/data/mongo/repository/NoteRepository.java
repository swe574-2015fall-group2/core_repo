package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.Note;
import com.boun.data.mongo.repository.custom.NoteRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<Note, String> , NoteRepositoryCustom{

}
