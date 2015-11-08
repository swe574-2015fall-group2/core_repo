package com.boun.http.request;

import com.boun.data.mongo.model.Group;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateGroupRequest extends BaseRequest{

	private Group group;
}
