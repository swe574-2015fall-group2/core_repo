package com.boun.http.request;

import java.util.Date;

import com.boun.data.common.enums.Status;

import lombok.Data;

@Data
public class CreateUserRequest{

    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private Status status;
    private Date birthDate;
    private String profession;
    private String university;
    private String programme;
    private String interestedAreas;
    private String linkedinProfile;
    private String academiaProfile;
}
