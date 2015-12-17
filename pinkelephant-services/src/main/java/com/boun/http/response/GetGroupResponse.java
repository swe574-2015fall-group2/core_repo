package com.boun.http.response;

import java.util.List;

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
	
	private List<String> tagList;

	private List<UserResponse> users;
}


