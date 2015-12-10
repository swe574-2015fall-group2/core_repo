package com.boun.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boun.data.Permission;
import com.boun.data.mongo.model.*;
import com.boun.http.request.*;
import com.boun.util.PermissionUtil;
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
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.MeetingRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetMeetingResponse;
import com.boun.http.response.ListMeetingResponse;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.PinkElephantTaggedService;
import com.boun.service.TagService;

@Service
public class MeetingServiceImpl extends PinkElephantTaggedService implements MeetingService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TagService tagService;

	@Override
	public void save(TaggedEntity entity) {
		meetingRepository.save((Meeting)entity);
	}
	
	@Override
	public Meeting findById(String meetingId) {
		Meeting meeting = meetingRepository.findOne(meetingId);

		if(meeting == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_NOT_FOUND, "");
		}

		return meeting;
	}

	@Override
	protected TagService getTagService() {
		return tagService;
	}
	
	@Override
	public CreateResponse createMeeting(CreateMeetingRequest request) {

		validate(request);

		PermissionUtil.checkPermission(request, request.getGroupId(), Permission.CREATE_MEETING);

		CreateResponse response = new CreateResponse();

		Group group = groupService.findById(request.getGroupId());
			
		Meeting meeting = mapRequestToMeeting(request);
		meeting.setGroup(group);
		meeting.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		
		meetingRepository.save(meeting);
		
		response.setAcknowledge(true);
		response.setEntityId(meeting.getId());
			
		tagService.tag(request.getTagList(), meeting, true);	
		
		return response;
	}
	
	@Override
	public ActionResponse inviteUser(InviteUserToMeetingRequest request) {
		ActionResponse response = new ActionResponse();

		validate(request);

		Meeting meeting = findById(request.getMeetingId());

		PermissionUtil.checkPermission(request, meeting.getGroup().getId(), Permission.INVITE_USER_TO_MEETING);

		//TODO check role of user
		//TODO send message/mail to invited user

		if(request.getUserIdList() == null || request.getUserIdList().isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "UserIdList is empty", "");
		}
		


		Set<User> invitedList = meeting.getInvitedUserSet();
		if(invitedList == null){
			invitedList = new HashSet<User>();
		}
		
		for (String id : request.getUserIdList()) {
			
			User user = userRepository.findOne(id);
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
		
		Meeting meeting = findById(request.getMeetingId());

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
		Meeting meeting = findById(request.getMeetingId());
		
		meeting.setName(request.getName());
		meeting.setActualDuration(request.getActualDuration());
		meeting.setAgendaSet(request.getAgendaSet());
		meeting.setDatetime(request.getDatetime());
		meeting.setTimezone(request.getTimezone());
		meeting.setDescription(request.getDescription());
		meeting.setEndHour(request.getEndHour());
		meeting.setStartHour(request.getStartHour());
		meeting.setLocation(request.getLocation());
		meeting.setStatus(request.getStatus());
		meeting.setType(request.getType());
		meeting.setTodoSet(request.getTodoSet());
		updateTag(meeting, request.getTagList());
		
		meetingRepository.save(meeting);
		
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ListMeetingResponse queryMeetingsOfGroup(BasicQueryRequest request) {
		
		validate(request);
		
		Group group = groupService.findById(request.getId());

		ListMeetingResponse response = new ListMeetingResponse();
		
		List<Meeting> meetingList = meetingRepository.findMeetings(group.getId());
		if(meetingList == null || meetingList.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_NOT_FOUND, "");
		}
		
		for (Meeting meeting : meetingList) {
			response.addMeeting(meeting);
		}
		response.setAcknowledge(true);
			
		return response;
	}
	
	@Override
	public GetMeetingResponse getMeeting(BasicQueryRequest request) {
		
		validate(request);
		
		Meeting meeting = findById(request.getId());

		GetMeetingResponse response = new GetMeetingResponse();
		
		response.setMeeting(new ListMeetingResponse.MeetingObj(meeting));
		response.setAcknowledge(true);
			
		return response;
	}
	
	@Override
	public ListMeetingResponse getMyMeetings(BaseRequest request) {
		
		validate(request);
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		List<String> meetingIdList = meetingRepository.getMeetingsOfUser(authenticatedUser.getId());

		ListMeetingResponse response = new ListMeetingResponse();
		
		for (String meetingId : meetingIdList) {
			Meeting meeting = findById(meetingId);
			response.addMeeting(meeting);
		}
		
		response.setAcknowledge(true);
			
		return response;
	}
	
	private Meeting mapRequestToMeeting(CreateMeetingRequest request){
		
		Meeting meeting = new Meeting();
		
		meeting.setName(request.getName());
		meeting.setAgendaSet(meeting.getAgendaSet());
		meeting.setDatetime(request.getDatetime());
		meeting.setStartHour(request.getStartHour());
		meeting.setEndHour(request.getEndHour());
		meeting.setDescription(request.getDescription());
		meeting.setLocation(request.getLocation());
		meeting.setType(request.getType());
		meeting.setStatus(MeetingStatus.NOT_STARTED);
		meeting.setTimezone(request.getTimezone());
		meeting.setTagList(request.getTagList());
		
		if(request.getInvitedUserIdList() == null || request.getInvitedUserIdList().isEmpty()){
			return meeting;
		}
		
		Set<User> invitedList = new HashSet<User>();
		for (String id : request.getInvitedUserIdList()) {
			
			User user = userRepository.findOne(id);
			if(user == null){
				throw new PinkElephantRuntimeException(400, ErrorCode.USER_NOT_FOUND, "");
			}
			
			if(!invitedList.contains(user)){
				invitedList.add(user);
			}
		}
		meeting.setInvitedUserSet(invitedList);

		return meeting;
	}
}
