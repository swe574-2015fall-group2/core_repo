package com.boun.http.request;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateResourceRequest extends BaseRequest{

	private String name;
	private String link;
	private String groupId;
	
	private List<TagData> tagList;
}
