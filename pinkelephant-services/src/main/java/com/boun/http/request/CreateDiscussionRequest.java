package com.boun.http.request;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateDiscussionRequest extends BaseRequest{

	private String groupId;
	private String name;
	private String description;
	private Boolean isPinned;
	private List<TagData> tagList;
}
