package com.boun.http.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boun.data.common.enums.MeetingStatus;
import com.boun.data.common.enums.MeetingType;
import com.boun.data.mongo.model.ContactDetails;
import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.Meeting;
import com.boun.data.mongo.model.User;
import com.boun.http.request.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListMeetingResponse extends ActionResponse{

	private List<MeetingObj> meetingList;

	public ListMeetingResponse() {
		meetingList = new ArrayList<>();
	}

	public void addMeeting(Meeting meeting, List<EntityRelation> meetingDiscussionList){
		if(meetingList == null){
			meetingList = new ArrayList<MeetingObj>();
		}
		meetingList.add(new MeetingObj(meeting, meetingDiscussionList));
	}
	
	public List<MeetingObj> getMeetingList(){
		Collections.sort(meetingList, new MeetingObjSort());
		return meetingList;
	}
	
	@Data
	public static class MeetingObj{
		private String id;
		private String creatorId;
		private String groupId;
		private String name;
		private Date datetime;
		private String timezone;
		private Set<String> agendaSet;
		private Set<String> todoSet;
		private String startHour;
		private String endHour;
		private Integer actualDuration;
		private String location;
		private String description;
		private MeetingStatus status;
		private MeetingType type;
		private Boolean isPinned;
		private ContactDetails contactDetails;
		
		private Set<User> invitedUserSet;
		private Set<User> attandedUserSet;
		private Set<User> rejectedUserSet;
		private Set<User> tentativeUserSet;
		
		private Set<String> discussionIdList;
		private Set<String> resourceIdList;
		
		private List<TagData> tagList;
		
		public MeetingObj(Meeting meeting, List<EntityRelation> meetingDiscussionList){
			this.id = meeting.getId();
			this.name = meeting.getName();
			this.description = meeting.getDescription();
			this.actualDuration = meeting.getActualDuration();
			this.agendaSet = meeting.getAgendaSet();
			this.attandedUserSet = meeting.getAttendedUserSet();
			this.datetime = meeting.getDatetime();
			this.timezone = meeting.getTimezone();
			this.startHour = meeting.getStartHour();
			this.endHour = meeting.getEndHour();
			this.invitedUserSet = meeting.getInvitedUserSet();
			this.rejectedUserSet = meeting.getRejectedUserSet();
			this.tentativeUserSet = meeting.getTentativeUserSet();
			this.location = meeting.getLocation();
			this.status = meeting.getStatus();
			this.todoSet = meeting.getTodoSet();
			this.type = meeting.getType();
			this.creatorId = meeting.getCreator().getId();
			this.groupId = meeting.getGroup().getId();
			this.tagList = meeting.getTagList();
			this.isPinned = meeting.getIsPinned();
			this.contactDetails = meeting.getContactDetails();
			
			if(meetingDiscussionList != null && !meetingDiscussionList.isEmpty()){
				this.discussionIdList = new HashSet<String>();
				this.resourceIdList = new HashSet<String>();
				for (EntityRelation meetingDiscussion : meetingDiscussionList) {
					
					if(meetingDiscussion.getDiscussion() != null){
						this.discussionIdList.add(meetingDiscussion.getDiscussion().getId());	
					}else if(meetingDiscussion.getResource() != null){
						this.resourceIdList.add(meetingDiscussion.getResource().getId());
					}
				}	
			}
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
	
	private static class MeetingObjSort implements Comparator<MeetingObj> {

	    @Override
	    public int compare(MeetingObj o1, MeetingObj o2) {
	    	if(o1.getDatetime() == null || o2.getDatetime() == null){
	    		return 1;
	    	}
	    	
	    	return (o1.getDatetime().before(o2.getDatetime())) ? -1 : 1;
	    }
	}

	public String toString() {
		return "";
	}
}


