package com.boun.http.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TagSearchRequest extends BasicSearchRequest{

	private TagData tagData;
}
