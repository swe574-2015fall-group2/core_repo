package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TagRequest extends BaseRequest{

	@NotNull
	private String entityId;
	
	@NotNull
	private TagData tag;
	
	@NotNull
	private boolean add;
	
}
