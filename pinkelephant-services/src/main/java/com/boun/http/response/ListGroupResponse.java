package com.boun.http.response;

import java.util.ArrayList;
import java.util.List;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupCount;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListGroupResponse extends ActionResponse{

	private List<GroupObj> groupList;
	
	public void addGroup(String id, String name, String description, Boolean isJoined, List<String> tagList){
		if(groupList == null){
			groupList = new ArrayList<GroupObj>();
		}
		groupList.add(new GroupObj(id, name, description, isJoined, tagList, null));
	}
	
	public void addGroup(GroupCount groupCount, boolean isJoined){
		Group group = groupCount.getGroup();
		if(groupList == null){
			groupList = new ArrayList<GroupObj>();
		}
		groupList.add(new GroupObj(group.getId(), group.getName(), group.getDescription(), isJoined, group.getTagList(), groupCount.getTotal()));
	}
	
	public void addGroup(String id, String name, String description, Boolean isJoined, List<String> tagList, long total){
		if(groupList == null){
			groupList = new ArrayList<GroupObj>();
		}
		groupList.add(new GroupObj(id, name, description, isJoined, tagList, total));
	}
	
	@Data
	public static class GroupObj{
		private String id;
		private String name;
		private String description;
		private Boolean joined;
		private List<String> tagList;
		private Long total;

		public GroupObj(String id, String name, String description, Boolean isJoined, List<String> tagList, Long total){
			this.id = id;
			this.name = name;
			this.description = description;
			this.setJoined(isJoined);
			this.tagList = tagList;
			this.total = total;
		}
		
	}
}


