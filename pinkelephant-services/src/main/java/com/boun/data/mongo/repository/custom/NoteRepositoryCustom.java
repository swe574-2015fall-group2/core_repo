package com.boun.data.mongo.repository.custom;

import com.boun.data.mongo.model.Note;

import java.util.List;

public interface NoteRepositoryCustom {

	List<Note> findNotes(String groupId);
	
	List<Note> findNotesByMeeting(String meetingId);

}
