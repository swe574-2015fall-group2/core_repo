package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
public class QueryResourceRequest extends BaseRequest{

	private String groupId;

	private String noteId;

	private String meetingId;

	private String discussionId;
}
