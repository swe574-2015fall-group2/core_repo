package com.boun.http.request;

import java.util.Date;

import lombok.Data;

@Data
public class UpdateUserRequest extends BaseRequest{

	private String id;

	private String firstname;

	private String lastname;

	private Date birthDate;

	private String profession;

	private String university;

	private String programme;

	private String interestedAreas;

	private String linkedinProfile;

	private String academiaProfile;

}
