package com.boun.http.response;

import com.boun.http.response.ListMeetingResponse.MeetingObj;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMeetingResponse extends ActionResponse {

	private MeetingObj meeting;
}
