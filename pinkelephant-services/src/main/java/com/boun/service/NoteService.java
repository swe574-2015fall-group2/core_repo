package com.boun.service;

import com.boun.data.mongo.model.Note;
import com.boun.http.request.CreateNoteRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateNoteRequest;
import com.boun.http.response.ActionResponse;

public interface NoteService {

    Note findById(String noteId);

    Note createNote(CreateNoteRequest request);

    Note updateNote(UpdateNoteRequest request);
    
    public ActionResponse tag(TagRequest request);;
    
}
