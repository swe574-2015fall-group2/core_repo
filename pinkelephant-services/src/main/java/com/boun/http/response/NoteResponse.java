package com.boun.http.response;

import com.boun.http.request.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteResponse {

	private String id;

	private String title;

	private String text;


	//group
	//meeting

	private Date createdAt;

	// creator

	private List<TagData> tagList;
}


