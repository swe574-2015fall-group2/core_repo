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
public class GetDiscussionResponse extends ActionResponse{

	private String id;
	private String name;
	private String description;
	private String creatorId;
	private String groupId;
	
	private List<Comment> commentList;
	
	public void addComment(String id, String comment, Date creationTime, String creatorId){
		if(commentList == null){
			commentList = new ArrayList<Comment>();
		}
		commentList.add(new Comment(id, comment, creationTime, creatorId));
	}
	
	@Data
	private static final class Comment{
		
		private String comment;
		private Date creationTime;
		private String creatorId;
		
		public Comment(String id, String comment, Date creationTime, String creatorId){
			this.comment = comment;
			this.creationTime = creationTime;
			this.creatorId = creatorId;
		}
		
	}
}


