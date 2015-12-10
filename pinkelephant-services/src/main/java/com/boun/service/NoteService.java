package com.boun.service;

import com.boun.data.mongo.model.Note;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateNoteRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateNoteRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.NoteResponse;

public interface NoteService {

    Note findById(String noteId);

    Note createNote(CreateNoteRequest request);

    Note updateNote(UpdateNoteRequest request);
    
    ActionResponse tag(TagRequest request);

    NoteResponse queryNote(BasicQueryRequest request);
    
}
