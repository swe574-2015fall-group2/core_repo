package com.boun.http.response;

import java.util.ArrayList;
import java.util.List;

import com.boun.data.mongo.model.User;
import com.boun.http.request.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetGroupResponse extends ActionResponse{

	private String id;
	private String name;
	private String description;
	private ImageData image;
	
	private List<TagData> tagList;

	private List<String> users;
	
	public void mapUsers(List<User> userList){
		if(userList == null || userList.isEmpty()){
			return;
		}
		
		if(users == null){
			users = new ArrayList<String>();
		}
		
		for (User user : userList) {
			users.add(user.getId());
		}
	}
}


