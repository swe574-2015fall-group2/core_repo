package com.boun.http.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ListGroupResponse extends ActionResponse{

	private List<GroupObj> groupList;
	
	public void addGroup(String id, String name, String description){
		if(groupList == null){
			groupList = new ArrayList<GroupObj>();
		}
		groupList.add(new GroupObj(id, name, description));
	}
	
	@Data
	private static class GroupObj{
		private String id;
		private String name;
		private String description;
		
		public GroupObj(String id, String name, String description){
			this.id = id;
			this.name = name;
			this.description = description;
		}
		
	}
}


