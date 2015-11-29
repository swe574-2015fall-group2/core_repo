package com.boun.http.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListGroupResponse extends ActionResponse{

	private List<GroupObj> groupList;
	
	public void addGroup(String id, String name, String description, Boolean isJoined){
		if(groupList == null){
			groupList = new ArrayList<GroupObj>();
		}
		groupList.add(new GroupObj(id, name, description, isJoined));
	}
	
	@Data
	public static class GroupObj{
		private String id;
		private String name;
		private String description;
		private Boolean joined;

		public GroupObj(String id, String name, String description, Boolean isJoined){
			this.id = id;
			this.name = name;
			this.description = description;
			this.setJoined(isJoined);
		}
		
	}
}


