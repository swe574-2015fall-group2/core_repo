package com.boun.http.request;

import com.boun.data.common.enums.MeetingType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateResourceRequest extends BaseRequest{

	private String name;

	private String link;
}
