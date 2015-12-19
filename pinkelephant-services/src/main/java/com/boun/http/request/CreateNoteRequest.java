package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateNoteRequest extends BaseRequest{

	@NotNull
	private String title;
	
	@NotNull
	private String text;
	
	private List<String> resourceIds;
	private String groupId;
	private String meetingId;
	private Boolean isPinned;

	private List<TagData> tagList;
}
