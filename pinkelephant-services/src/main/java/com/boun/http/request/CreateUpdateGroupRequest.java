package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateUpdateGroupRequest extends BaseRequest{

	private String name;

	private String description;
}
