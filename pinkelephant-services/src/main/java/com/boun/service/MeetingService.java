package com.boun.service;

import com.boun.data.mongo.model.Meeting;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.TagRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetMeetingResponse;
import com.boun.http.response.ListMeetingResponse;

public interface MeetingService {

	Meeting findById(String groupId);

	CreateResponse createMeeting(CreateMeetingRequest request);
	
	ActionResponse updateMeeting(UpdateMeetingRequest request);
	
	ActionResponse inviteUser(InviteUserToMeetingRequest request);
	
	ListMeetingResponse queryMeetingsOfGroup(BasicQueryRequest request);
	
	ActionResponse invitationReply(MeetingInvitationReplyRequest request);
	
	GetMeetingResponse getMeeting(BasicQueryRequest request);
	
	ActionResponse tag(TagRequest request);
	
	ListMeetingResponse getMyMeetings(BaseRequest request);
}
