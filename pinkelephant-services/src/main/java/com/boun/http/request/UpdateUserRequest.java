package com.boun.http.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateUserRequest extends BaseRequest{

	@NotNull
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
