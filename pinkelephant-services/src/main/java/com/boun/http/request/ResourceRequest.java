package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper=false)
public class ResourceRequest extends BaseRequest{

	@NotNull
	private String entityId;

	@NotNull
	private TagData tag;

	@NotNull
	private boolean add;
	
}
