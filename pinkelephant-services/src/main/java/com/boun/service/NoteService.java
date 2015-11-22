package com.boun.service;

import com.boun.data.mongo.model.Note;
import com.boun.data.mongo.model.Resource;
import com.boun.http.request.CreateNoteRequest;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.request.DeleteResourceRequest;
import com.boun.http.request.QueryResourceRequest;

import java.util.List;

public interface NoteService {

    Note findById(String noteId);

    Note createNote(CreateNoteRequest request);
    
}
