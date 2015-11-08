package com.boun.data.mongo.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@Document(collection = "user_detail")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDetail {

    private Date birthDate;

    private String profession;

    private String university;

    private String programme;

    private String interestedAreas;

    private String linkedinProfile;

    private String academiaProfile;

    private String imagePath;
}
