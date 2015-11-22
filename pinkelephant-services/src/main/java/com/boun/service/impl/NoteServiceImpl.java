package com.boun.service.impl;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.enums.ResourceType;
import com.boun.data.mongo.model.*;
import com.boun.data.mongo.repository.NoteRepository;
import com.boun.data.mongo.repository.RoleRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateNoteRequest;
import com.boun.http.request.CreateRoleRequest;
import com.boun.http.request.UpdateRoleRequest;
import com.boun.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoteServiceImpl extends PinkElephantService implements NoteService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private GroupService groupService;

	@Autowired
	private MeetingService meetingService;

	@Autowired
	private ResourceService resourceService;

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

		note = noteRepository.save(note);

		return note;

	}

}
