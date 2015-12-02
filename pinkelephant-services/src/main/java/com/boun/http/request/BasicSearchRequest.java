package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class BasicSearchRequest extends BaseRequest{

	private String queryString;
}
