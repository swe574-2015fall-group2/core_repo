package com.boun.web.controller;


import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserDetail;
import com.boun.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@Api(value = "user", description = "User Service")
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get own profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 404, message = "Resource not found"),
            @ApiResponse(code = 401, message = "Not Authorized"),
    })
    public User getUser(HttpServletRequest request) {

        User user = new User();

        user.setUsername("barisozcanli");
        user.setFirstNname("Barış");
        user.setLastName("Özcanlı");
        user.setPassword("*****");

        UserDetail detail = new UserDetail();
        detail.setAcademiaProfile("http://academia/...");
        detail.setBirthDate(new Date());
        detail.setImagePath("http://www.toimage.com/...");
        detail.setInterestedAreas("slackline, software engineering, guitar, books, everything everything");
        detail.setLinkedinProfile("http://www.linkedin.com/barisozcanli");
        detail.setProfession("software");
        detail.setProgramme("software engineering");
        detail.setUniversity("Boun");

        user.setUserDetail(detail);

        userService.save(user);

        return user;
    }

}
