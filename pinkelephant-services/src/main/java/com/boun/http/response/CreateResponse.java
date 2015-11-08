package com.boun.http.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateResponse extends ActionResponse{

	private String entityId;
}
