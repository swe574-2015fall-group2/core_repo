package com.boun.http.request;

import com.boun.data.common.enums.Status;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserDetail;
import com.boun.data.mongo.model.UserPreferences;
import com.boun.data.mongo.model.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Set;

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

	private String imagePath;

}
