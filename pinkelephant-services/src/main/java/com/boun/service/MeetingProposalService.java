package com.boun.service;

import com.boun.data.mongo.model.MeetingProposal;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingProposalRequest;
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.MeetingProposalInvitationReplyRequest;
import com.boun.http.request.UpdateMeetingProposalRequest;
import com.boun.http.response.ActionResponse;

public interface MeetingProposalService {

	public ActionResponse createMeetingProposal(CreateMeetingProposalRequest request);
	
	public ActionResponse updateMeetingProposal(UpdateMeetingProposalRequest request);
	
	public ActionResponse deleteMeetingProposal(BasicDeleteRequest request);
	
	public ActionResponse getMeetingProposal(BasicQueryRequest request);
	
	public ActionResponse replyInvitation(MeetingProposalInvitationReplyRequest request);
	
	public MeetingProposal findById(String id);
}
