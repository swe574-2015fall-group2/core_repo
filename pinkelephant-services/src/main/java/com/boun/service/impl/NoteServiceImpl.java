package com.boun.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.Note;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.repository.NoteRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateNoteRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateNoteRequest;
import com.boun.http.response.ActionResponse;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.NoteService;
import com.boun.service.PinkElephantTaggedService;
import com.boun.service.ResourceService;
import com.boun.service.TagService;

@Service
public class NoteServiceImpl extends PinkElephantTaggedService implements NoteService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private TagService tagService;

	@Override
	public Note findById(String id) {
		Note note = noteRepository.findOne(id);

		if(note == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.NOTE_NOT_FOUND, "todo dev message");
		}

		return note;
	}

	@Override
	public Note createNote(CreateNoteRequest request) {

		validate(request);

		Group group = groupService.findById(request.getGroupId());
		//TODO check if user is in this group

		Meeting meeting = meetingService.findById(request.getMeetingId());
		List<Resource> resources = resourceService.findByIds(request.getResourceIds());

		Note note = new Note();

		note.setTitle(request.getTitle());
		note.setText(request.getText());
		note.setGroup(group);
		note.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		note.setCreatedAt(new Date());
		note.setMeeting(meeting);
		note.setResources(resources);
		note.setTagList(request.getTagList());
		
		note = noteRepository.save(note);
		tagService.tag(request.getTagList(), note, true);

		return note;

	}

	@Override
	public Note updateNote(UpdateNoteRequest request) {

		validate(request);

		Note note = findById(request.getId());

		Group group = groupService.findById(request.getGroupId());
		//TODO check if user is in this group

		Meeting meeting = meetingService.findById(request.getMeetingId());
		List<Resource> resources = resourceService.findByIds(request.getResourceIds());

		note.setTitle(request.getTitle());
		note.setText(request.getText());
		note.setGroup(group);
		//note.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		//note.setCreatedAt(new Date());
		//TODO modifiedDate, modifiedBy

		note.setMeeting(meeting);
		note.setResources(resources);

		note = noteRepository.save(note);

		return note;
	}

	@Override
	public ActionResponse tag(TagRequest request) {
		
		validate(request);
		
		Note note = findById(request.getEntityId());
		
		ActionResponse response = new ActionResponse();
		if(tag(note, request.getTag(), request.isAdd())){
			response.setAcknowledge(true);
			
			noteRepository.save(note);
		}
		
		return response;
	}
	
	@Override
	protected TagService getTagService() {
		return tagService;
	}
}
