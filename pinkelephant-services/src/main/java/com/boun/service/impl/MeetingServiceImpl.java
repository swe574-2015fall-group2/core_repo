package com.boun.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.enums.MeetingInvitationResult;
import com.boun.data.common.enums.MeetingStatus;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.MeetingRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.QueryMeetingRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.ListMeetingResponse;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.PinkElephantService;

@Service
public class MeetingServiceImpl extends PinkElephantService implements MeetingService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public CreateResponse createMeeting(CreateMeetingRequest request) {

		validate(request);

		CreateResponse response = new CreateResponse();

		Group group = groupService.findById(request.getGroupId());
		if(group == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}
			
		Meeting meeting = mapRequestToMeeting(request);
		meeting.setGroup(group);
		meeting.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		
		meetingRepository.save(meeting);
		
		response.setAcknowledge(true);
		response.setEntityId(meeting.getId());
			
		return response;
	}
	
	@Override
	public ActionResponse inviteUser(InviteUserToMeetingRequest request) {
		ActionResponse response = new ActionResponse();

		validate(request);

		//TODO check role of user
		//TODO send message/mail to invited user

		if(request.getUsernameList() == null || request.getUsernameList().isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "UsernameList is empty", "");
		}
		
		Meeting meeting = meetingRepository.findOne(request.getMeetingId());
		if(meeting == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_NOT_FOUND, "");
		}

		Set<User> invitedList = meeting.getInvitedUserSet();
		if(invitedList == null){
			invitedList = new HashSet<User>();
		}
		
		for (String username : request.getUsernameList()) {
			
			User user = userRepository.findByUsername(username);
			if(user == null){
				throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
			}
			
			if(!invitedList.contains(user)){
				invitedList.add(user);
			}
		}
		meeting.setInvitedUserSet(invitedList);
		
		meetingRepository.save(meeting);
		
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ActionResponse invitationReply(MeetingInvitationReplyRequest request) {
		ActionResponse response = new ActionResponse();

		validate(request);

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		Meeting meeting = meetingRepository.findOne(request.getMeetingId());
		if(meeting == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_NOT_FOUND, "");
		}

		Set<User> invitedList = meeting.getInvitedUserSet();
		if(invitedList == null || invitedList.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.THERE_IS_NO_INVITED_PERSON, "");
		}
		
		if(!invitedList.contains(authenticatedUser)){
			throw new PinkElephantRuntimeException(400, ErrorCode.NOT_INVITED_TO_MEETING, "");
		}
		
		if(request.getResult().value() == MeetingInvitationResult.ACCEPT.value()){
		
			checkMeetingInvitation(meeting.getAttendedUserSet(), authenticatedUser, ErrorCode.ALREADY_ACCEPTED_INVITATION);
			
			Set<User> list = addToMeetingList(meeting.getAttendedUserSet(), authenticatedUser);
			
			removeFromMeetingList(meeting.getRejectedUserSet(), authenticatedUser);
			removeFromMeetingList(meeting.getTentativeUserSet(), authenticatedUser);
			
			meeting.setAttendedUserSet(list);
			
		} else if(request.getResult().value() == MeetingInvitationResult.REJECT.value()){
			
			checkMeetingInvitation(meeting.getRejectedUserSet(), authenticatedUser, ErrorCode.ALREADY_REJECTED_INVITATION);
			
			Set<User> list = addToMeetingList(meeting.getRejectedUserSet(), authenticatedUser);
			
			removeFromMeetingList(meeting.getAttendedUserSet(), authenticatedUser);
			removeFromMeetingList(meeting.getTentativeUserSet(), authenticatedUser);
			
			meeting.setRejectedUserSet(list);
			
		} else if(request.getResult().value() == MeetingInvitationResult.TENTATIVE.value()){
			
			checkMeetingInvitation(meeting.getTentativeUserSet(), authenticatedUser, ErrorCode.ALREADY_MARKED_AS_TENTATIVE_INVITATION);
			
			Set<User> list = addToMeetingList(meeting.getTentativeUserSet(), authenticatedUser);
			
			removeFromMeetingList(meeting.getAttendedUserSet(), authenticatedUser);
			removeFromMeetingList(meeting.getRejectedUserSet(), authenticatedUser);
			
			meeting.setTentativeUserSet(list);
		}
		
		meetingRepository.save(meeting);
		
		response.setAcknowledge(true);

		return response;
	}
	
	private void checkMeetingInvitation(Set<User> userList, User authenticatedUser, ErrorCode errorCode){
		if(userList != null && !userList.isEmpty()){
			if(userList.contains(authenticatedUser)){
				throw new PinkElephantRuntimeException(400, errorCode, "");	
			}
		}
	}
	
	private Set<User> addToMeetingList(Set<User> userList, User authenticatedUser){
		if(userList == null || userList.isEmpty()){
			userList = new HashSet<User>();
		}
		userList.add(authenticatedUser);
		
		return userList;
	}
	
	private Set<User> removeFromMeetingList(Set<User> userList, User authenticatedUser){
		if(userList == null || userList.isEmpty()){
			return null;
		}
		
		userList.remove(authenticatedUser);
		
		return userList;
	}
	
	@Override
	public ActionResponse updateMeeting(UpdateMeetingRequest request) {
		
		validate(request);

		ActionResponse response = new ActionResponse();
		Meeting meeting = meetingRepository.findOne(request.getMeetingId());
		if(meeting == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_NOT_FOUND, "");
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
		if(request.getType() != null){
			meeting.setType(request.getType());
		}
		if(request.getTodoSet() != null){
			meeting.setTodoSet(request.getTodoSet());
		}
		
		meetingRepository.save(meeting);
		
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ListMeetingResponse queryMeetingsOfGroup(QueryMeetingRequest request) {
		
		validate(request);
		
		//TODO sort response according to date 
		
		ListMeetingResponse response = new ListMeetingResponse();
		
		List<Meeting> meetingList = meetingRepository.findMeetings(request.getGroupId());
		if(meetingList == null || meetingList.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_NOT_FOUND, "");
		}
		
		for (Meeting meeting : meetingList) {
			response.addMeeting(meeting);
		}
		response.setAcknowledge(true);
			
		return response;
	}
	
	private Meeting mapRequestToMeeting(CreateMeetingRequest request){
		
		Meeting meeting = new Meeting();
		
		meeting.setAgendaSet(meeting.getAgendaSet());
		meeting.setDatetime(request.getDatetime());
		meeting.setDescription(request.getDescription());
		meeting.setEstimatedDuration(request.getEstimatedDuration());
		meeting.setLocation(request.getLocation());
		meeting.setType(request.getType());
		meeting.setStatus(MeetingStatus.NOT_STARTED);
		
		return meeting;
	}
}