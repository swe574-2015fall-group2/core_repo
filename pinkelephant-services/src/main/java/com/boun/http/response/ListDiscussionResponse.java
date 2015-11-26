package com.boun.http.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDiscussionResponse extends ActionResponse{

	private List<DiscussionObj> discussionList;
	
	public void addDiscussion(String id, String name, String description, String creatorId, Date creationTime){
		if(discussionList == null){
			discussionList = new ArrayList<DiscussionObj>();
		}
		discussionList.add(new DiscussionObj(id, name, description, creatorId, creationTime));
	}
	
	@Data
	private static class DiscussionObj{
		private String id;
		private String name;
		private String description;
		private String creatorId;
		private Date creationTime;
		
		public DiscussionObj(String id, String name, String description, String creatorId, Date creationTime){
			this.id = id;
			this.name = name;
			this.description = description;
			this.creatorId = creatorId;
			this.creationTime = creationTime;
		}
		
	}
}


