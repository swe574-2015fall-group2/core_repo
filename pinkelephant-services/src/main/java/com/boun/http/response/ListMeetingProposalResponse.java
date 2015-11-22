package com.boun.http.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boun.data.mongo.model.MeetingProposal;
import com.boun.data.mongo.model.MeetingProposal.Respondant;
import com.boun.data.mongo.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListMeetingProposalResponse extends ActionResponse{

	private List<MeetingProposalObj> dataList;
	
	public void addMeetingProposal(MeetingProposal proposal){
		if(dataList == null){
			dataList = new ArrayList<MeetingProposalObj>();
		}
		dataList.add(new MeetingProposalObj(proposal));
	}
	
	@Data
	private static class MeetingProposalObj{
		
		private String message;
		private Date datetime;
		private boolean status;
		
		private String creatorId;
		private String discussionId;
		
		private List<RespondantObj> respondantList;
		
		public MeetingProposalObj(MeetingProposal meetingProposal){
			this.message = meetingProposal.getMessage();
			this.datetime = meetingProposal.getDatetime();
			this.status = meetingProposal.isStatus();
			this.creatorId = meetingProposal.getCreator().getId();
			this.discussionId = meetingProposal.getDiscussion().getId();
			
			for (Respondant respondant : meetingProposal.getRespondantList()) {
				addRespondant(respondant.getUser(), respondant.isResponse());
			}
		}

		public void addRespondant(User user, boolean status){
			if(respondantList == null){
				respondantList = new ArrayList<RespondantObj>();
			}
			respondantList.add(new RespondantObj(user.getId(), status));
		}
	}
	
	
	@Data
	private static class RespondantObj{
		
		private String userId;
		private boolean response;
		
		public RespondantObj(String userId, boolean response){
			this.userId = userId;
			this.response = response;
		}
	}
}


