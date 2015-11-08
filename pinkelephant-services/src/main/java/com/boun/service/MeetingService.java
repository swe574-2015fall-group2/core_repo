package com.boun.service;

import com.boun.http.request.CreateMeetingRequest;
import com.boun.http.request.InviteUserToMeetingRequest;
import com.boun.http.request.UpdateMeetingRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;

public interface MeetingService {

	public CreateResponse createMeeting(CreateMeetingRequest request);
	
	public ActionResponse updateMeeting(UpdateMeetingRequest request);
	
	public ActionResponse inviteUser(InviteUserToMeetingRequest request);
}
