package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateResourceRequest extends BaseRequest{

	private String name;

	private String link;

	private String groupId;
}
