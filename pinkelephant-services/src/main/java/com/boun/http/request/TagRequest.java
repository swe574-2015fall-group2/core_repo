package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TagRequest extends BaseRequest{

	private String entityId;
	private String tag;
	private boolean add;
	
}
