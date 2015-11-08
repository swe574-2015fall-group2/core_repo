package com.boun.http.request;

import com.boun.data.mongo.model.Group;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateGroupRequest extends BaseRequest{

	Group group;
}
