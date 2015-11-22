package com.boun.service;

import com.boun.data.mongo.model.Note;
import com.boun.data.mongo.model.Resource;
import com.boun.http.request.*;

import java.util.List;

public interface NoteService {

    Note findById(String noteId);

    Note createNote(CreateNoteRequest request);

    Note updateNote(UpdateNoteRequest request);
    
}
