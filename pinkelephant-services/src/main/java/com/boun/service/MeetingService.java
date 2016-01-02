package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.LinkRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetMeetingResponse;
import com.boun.http.response.ListMeetingResponse;

public interface MeetingService {

	Meeting findById(String meetingId);

	CreateResponse createMeeting(CreateMeetingRequest request);
	
	ActionResponse updateMeeting(UpdateMeetingRequest request);
	
	ActionResponse inviteUser(InviteUserToMeetingRequest request);
	
	ListMeetingResponse queryMeetingsOfGroup(BasicQueryRequest request);
	
	ActionResponse invitationReply(MeetingInvitationReplyRequest request);
	
	GetMeetingResponse getMeeting(BasicQueryRequest request);
	
	ActionResponse tag(TagRequest request);
	
	ActionResponse link(LinkRequest request, EntityType toType);
	
	ActionResponse removeLink(LinkRequest request, EntityType toType);
	
	ListMeetingResponse getMyMeetings(BaseRequest request);
	
	List<Meeting> findAllMeetingList();
}
