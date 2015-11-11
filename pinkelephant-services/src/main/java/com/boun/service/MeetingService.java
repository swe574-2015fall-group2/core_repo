package com.boun.service;

import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.MeetingInvitationReplyRequest;
import com.boun.http.request.QueryMeetingRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.ListMeetingResponse;

public interface MeetingService {

	public CreateResponse createMeeting(CreateMeetingRequest request);
	
	public ActionResponse updateMeeting(UpdateMeetingRequest request);
	
	public ActionResponse inviteUser(InviteUserToMeetingRequest request);
	
	public ListMeetingResponse queryMeetingsOfGroup(QueryMeetingRequest request);
	
	public ActionResponse invitationReply(MeetingInvitationReplyRequest request);
}
