package com.boun.service.impl;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.GroupRepository;
import com.boun.data.mongo.repository.MeetingRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.service.MeetingService;
import com.boun.service.PinkElephantService;

@Service
public class MeetingServiceImpl extends PinkElephantService implements MeetingService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeetingRepository meetingRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ActionResponse createMeeting(CreateMeetingRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			Group group = groupRepository.findByGroupName(request.getGroupName());
			if(group == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.GROUP_NOT_FOUND.getMessage());
				return response;
			}
			
			Meeting meeting = mapRequestToMeeting(request);
			meeting.setGroup(group);
			
			meetingRepository.save(meeting);
			
			response.setAcknowledge(true);
			response.setEntityId(meeting.getId());
			
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in createMeeting()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse inviteUser(InviteUserToMeetingRequest request) {
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			if(request.getUsernameList() == null || request.getUsernameList().isEmpty()){
				response.setAcknowledge(false);
				response.setMessage(MessageFormat.format(ErrorCode.INVALID_INPUT.getMessage(), "UsernameList is empty"));
				return response;
			}
			
			Meeting meeting = meetingRepository.findOne(request.getMeetingId());
			if(meeting == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.MEETING_NOT_FOUND.getMessage());
				return response;
			}

			Set<User> invitedList = meeting.getInvitedUserSet();
			if(invitedList == null){
				invitedList = new HashSet<User>();
			}
			
			System.out.println("meeting.getGroup().getDescription()->" + meeting.getGroup().getDescription());
			
			for (String username : request.getUsernameList()) {
				
				User user = userRepository.findByUsername(username);
				if(user == null){
					response.setAcknowledge(false);
					response.setMessage(ErrorCode.USER_NOT_FOUND.getMessage());
					return response;
				}
				
				if(!invitedList.contains(user)){
					invitedList.add(user);
				}
			}
			meeting.setInvitedUserSet(invitedList);
			
			meetingRepository.save(meeting);
			
			response.setAcknowledge(true);
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in inviteUser()", e);
		}

		return response;
	}
	
	@Override
	public ActionResponse updateMeeting(UpdateMeetingRequest request) {
		
		ActionResponse response = new ActionResponse();

		if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
			response.setAcknowledge(false);
			response.setMessage(ErrorCode.OPERATION_NOT_ALLOWED.getMessage());
			return response;
		}

		try {
			Meeting meeting = meetingRepository.findOne(request.getMeetingId());
			if(meeting == null){
				response.setAcknowledge(false);
				response.setMessage(ErrorCode.MEETING_NOT_FOUND.getMessage());
				return response;
			}
			
			if(request.getActualDuration() != null){
				meeting.setActualDuration(request.getActualDuration());
			}
			if(request.getAgendaSet() != null && !request.getAgendaSet().isEmpty()){
				meeting.setAgendaSet(request.getAgendaSet());
			}
			if(request.getDatetime() != null){
				meeting.setDatetime(request.getDatetime());
			}
			if(request.getDescription()!=null && !request.getDescription().equalsIgnoreCase("")){
				meeting.setDescription(request.getDescription());
			}
			if(request.getEstimatedDuration() != null){
				meeting.setEstimatedDuration(request.getEstimatedDuration());
			}
			if(request.getLocation() != null && !request.getLocation().equalsIgnoreCase("")){
				meeting.setLocation(request.getLocation());
			}
			if(request.getStatus() != null){
				meeting.setStatus(request.getStatus());
			}
			
			meetingRepository.save(meeting);
			
			response.setAcknowledge(true);
			response.setEntityId(meeting.getId());
			
		} catch (Throwable e) {
			response.setAcknowledge(false);
			response.setMessage(e.getMessage());

			logger.error("Error in updateMeeting()", e);
		}

		return response;
		
	}

	private Meeting mapRequestToMeeting(CreateMeetingRequest request){
		
		Meeting meeting = new Meeting();
		
		meeting.setAgendaSet(meeting.getAgendaSet());
		meeting.setDatetime(request.getDatetime());
		meeting.setDescription(request.getDescription());
		meeting.setEstimatedDuration(request.getEstimatedDuration());
		meeting.setLocation(request.getLocation());
		
		return meeting;
	}
}
