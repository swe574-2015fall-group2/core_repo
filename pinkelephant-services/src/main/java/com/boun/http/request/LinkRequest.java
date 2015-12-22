package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class LinkRequest extends BaseRequest{

	@NotNull
	private String fromEntityId;
	
	@NotNull
	private String toEntityId;
}
