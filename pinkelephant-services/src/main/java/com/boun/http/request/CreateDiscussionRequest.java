package com.boun.http.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateDiscussionRequest extends BaseRequest{

	@NotNull
	private String groupId;
	
	@NotNull
	@Size(min=1, max=1000)
	private String name;
	
	@NotNull
	private String description;
	
	private List<String> meetingIdList;
	private List<String> resourceIdList;
	
	private Boolean isPinned;
	private List<TagData> tagList;
}
