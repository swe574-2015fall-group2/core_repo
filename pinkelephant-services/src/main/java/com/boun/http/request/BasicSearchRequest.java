package com.boun.http.request;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BasicSearchRequest extends BaseRequest{

	@NotNull
	private String queryString;
}
