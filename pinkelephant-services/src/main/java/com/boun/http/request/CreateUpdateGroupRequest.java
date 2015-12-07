package com.boun.http.request;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateUpdateGroupRequest extends BaseRequest{

	private String groupId;
	private String name;
	private String description;
	
	private List<String> tagList;
}
