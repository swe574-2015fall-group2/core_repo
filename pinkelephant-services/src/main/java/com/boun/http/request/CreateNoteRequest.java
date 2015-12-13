package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateNoteRequest extends BaseRequest{

	private String title;
	private String text;
	private List<String> resourceIds;
	private String groupId;
	private String meetingId;
	private Boolean isPinned;

	private List<String> tagList;
}
