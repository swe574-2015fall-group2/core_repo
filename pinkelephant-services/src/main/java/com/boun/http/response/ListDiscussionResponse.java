package com.boun.http.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.EntityRelation.RelationType;
import com.boun.http.request.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDiscussionResponse extends ActionResponse{

	private List<DiscussionObj> discussionList;
	
	public void addDiscussion(String id, String name, String description, String creatorId, Date creationTime, List<TagData> tagList, Boolean isPinned, List<EntityRelation> meetingDiscussionList){
		if(discussionList == null){
			discussionList = new ArrayList<DiscussionObj>();
		}
		discussionList.add(new DiscussionObj(id, name, description, creatorId, creationTime, tagList, isPinned, meetingDiscussionList));
	}
	
	@Data
	private static class DiscussionObj{
		private String id;
		private String name;
		private String description;
		private String creatorId;
		private Date creationTime;
		private Boolean isPinned;
		private List<TagData> tagList;
		private List<String> meetingIdList;
		private List<String> resourceIdList;
		
		public DiscussionObj(String id, String name, String description, String creatorId, Date creationTime, List<TagData> tagList, Boolean isPinned, List<EntityRelation> meetingDiscussionList){
			this.id = id;
			this.name = name;
			this.description = description;
			this.creatorId = creatorId;
			this.creationTime = creationTime;
			this.isPinned = isPinned;
			this.tagList = tagList;
			
			if(meetingDiscussionList != null && !meetingDiscussionList.isEmpty()){
				this.meetingIdList = new ArrayList<String>();
				this.resourceIdList = new ArrayList<String>();
				for (EntityRelation meetingDiscussion : meetingDiscussionList) {
					
					if(meetingDiscussion.getToType() == RelationType.MEETING){
						this.meetingIdList.add(meetingDiscussion.getEntityTo().getId());	
					}else if(meetingDiscussion.getToType() == RelationType.RESOURCE){
						this.resourceIdList.add(meetingDiscussion.getEntityTo().getId());
					}
					
				}
			}
		}
		
	}
}


