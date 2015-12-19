package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateNoteRequest extends BaseRequest{

	@NotNull
	private String id;

	private String title;

	private String text;

	private List<String> resourceIds;

	private String groupId;

	private String meetingId;

	private List<TagData> tagList;

	private Boolean isPinned;
}
