package com.boun.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.boun.data.Permission;
import com.boun.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Discussion;
import com.boun.data.mongo.model.MeetingProposal;
import com.boun.data.mongo.model.MeetingProposal.Respondant;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.MeetingProposalRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateMeetingProposalRequest;
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.MeetingProposalInvitationReplyRequest;
import com.boun.http.request.UpdateMeetingProposalRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.ListMeetingProposalResponse;
import com.boun.service.DiscussionService;
import com.boun.service.MeetingProposalService;
import com.boun.service.PinkElephantService;

@Service
public class MeetingProposalServiceImpl extends PinkElephantService implements MeetingProposalService{

	@Autowired
	private MeetingProposalRepository meetingProposalRepository;
	
	@Autowired
	private DiscussionService discussionService;
	
	@Override
	public ActionResponse createMeetingProposal(CreateMeetingProposalRequest request){
		
		ActionResponse response = new ActionResponse();
		
		validate(request);

		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		Discussion discussion = discussionService.findById(request.getDiscussionId());

		//PermissionUtil.checkPermission(request, discussion.getGroup().getId(), Permission.CREATE_MEETING_PROPOSAL);

		MeetingProposal proposal = new MeetingProposal();
		proposal.setDiscussion(discussion);
		proposal.setCreator(authenticatedUser);
		proposal.setDatetime(request.getDatetime());
		proposal.setMessage(request.getMessage());
		proposal.setStatus(true);
		
		meetingProposalRepository.save(proposal);
		
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ActionResponse updateMeetingProposal(UpdateMeetingProposalRequest request){
		
		ActionResponse response = new ActionResponse();
		
		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		MeetingProposal proposal = findById(request.getId());

		//PermissionUtil.checkPermission(request, proposal.getDiscussion().getGroup().getId(), Permission.CREATE_MEETING_PROPOSAL);

		if(!authenticatedUser.isEqual(proposal.getCreator())){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Input userId is different than authenticated user", "");
		}
		
		if(request.getDatetime() != null){
			proposal.setDatetime(request.getDatetime());	
		}
		
		if(request.getMessage() != null){
			proposal.setMessage(request.getMessage());	
		}
		
		proposal.setStatus(request.isStatus());
		
		meetingProposalRepository.save(proposal);
		
		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ActionResponse deleteMeetingProposal(BasicDeleteRequest request) {
		
		ActionResponse response = new ActionResponse();
		
		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		MeetingProposal meetingProposal = findById(request.getId());
		
		if(!authenticatedUser.isEqual(meetingProposal.getCreator())){
			throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Input userId is different than authenticated user", "");
		}
		
		meetingProposalRepository.delete(meetingProposal);
		
		response.setAcknowledge(true);
		return response;
	}

	@Override
	public ActionResponse replyInvitation(MeetingProposalInvitationReplyRequest request) {
		
		ActionResponse response = new ActionResponse();
		
		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		MeetingProposal meetingProposal = findById(request.getMeetingProposalId());
		
		List<Respondant> respondantList = meetingProposal.getRespondantList();
		
		if(respondantList == null || respondantList.isEmpty()){
			respondantList = new ArrayList<Respondant>();
			respondantList.add(new Respondant(authenticatedUser, request.isReponse()));
		}else{
			boolean found = false;
			for (Respondant respondant : respondantList) {
				
				if(respondant.getUser().isEqual(authenticatedUser)){
					found = true;
					
					if(request.isReponse() == respondant.isResponse()){
						//Already responded
						response.setAcknowledge(true);
						return response;
					}
					
					respondant.setResponse(request.isReponse());
					break;
				}
			}
			
			if(!found){
				respondantList.add(new Respondant(authenticatedUser, request.isReponse()));	
			}
		}
		
		meetingProposal.setRespondantList(respondantList);
		
		meetingProposalRepository.save(meetingProposal);
		
		response.setAcknowledge(true);
		return response;
	}

	@Override
	public MeetingProposal findById(String id) {
		MeetingProposal meetingProposal = meetingProposalRepository.findOne(id);
		
		if(meetingProposal == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_PROPOSAL_NOT_FOUND, "");
		}
		return meetingProposal;
	}

	@Override
	public ListMeetingProposalResponse getMeetingProposal(BasicQueryRequest request) {
		
		validate(request);
		
		Discussion discussion = discussionService.findById(request.getId());
		List<MeetingProposal> resultList = meetingProposalRepository.findMeetingProposals(discussion.getId());
		
		ListMeetingProposalResponse response = new ListMeetingProposalResponse();
		if(resultList == null || resultList.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.MEETING_PROPOSAL_NOT_FOUND, "");
		}
		
		for (MeetingProposal meetingProposal : resultList) {
			response.addMeetingProposal(meetingProposal);	
		}
		response.setAcknowledge(true);
		return response;
	}

}
