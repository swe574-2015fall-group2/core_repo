package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TagRequest extends BaseRequest{

	private String entityId;
	private TagData tag;
	private boolean add;
	
}
