package com.boun.http.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boun.data.common.enums.MeetingStatus;
import com.boun.data.common.enums.MeetingType;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListMeetingResponse extends ActionResponse{

	private List<MeetingObj> meetingList;
	
	public void addMeeting(Meeting meeting){
		if(meetingList == null){
			meetingList = new ArrayList<MeetingObj>();
		}
		meetingList.add(new MeetingObj(meeting));
	}
	
	@Data
	private static class MeetingObj{
		private String id;
		private Date datetime;
		private Set<String> agendaSet;
		private Set<String> todoSet;
		private Integer estimatedDuration;
		private Integer actualDuration;
		private String location;
		private String description;
		private MeetingStatus status;
		private MeetingType type;
		
		private Set<String> invitedUserSet;
		private Set<String> attandedUserSet;
		
		public MeetingObj(Meeting meeting){
			this.id = meeting.getId();
			this.description = meeting.getDescription();
			this.actualDuration = meeting.getActualDuration();
			this.agendaSet = meeting.getAgendaSet();
			this.attandedUserSet = getUsernameSet(meeting.getAttendedUserSet());
			this.datetime = meeting.getDatetime();
			this.estimatedDuration = meeting.getEstimatedDuration();
			this.invitedUserSet = getUsernameSet(meeting.getInvitedUserSet());
			this.location = meeting.getLocation();
			this.status = meeting.getStatus();
			this.todoSet = meeting.getTodoSet();
			this.type = meeting.getType();
		}
		
		private Set<String> getUsernameSet(Set<User> userSet){
			
			if(userSet == null || userSet.isEmpty()){
				return null;
			}
			
			Set<String> nameSet = new HashSet<String>();
			for (User user : userSet) {
				nameSet.add(user.getUsername());
			}
			return nameSet;
		}
	}
}


