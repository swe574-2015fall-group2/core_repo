package com.boun.service;

import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserMetadata;
import com.boun.http.request.CreateProfileRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface UserService {

    User createProfile(CreateProfileRequest createProfile, Long userId);

    List<User> save(Collection<User> users);

    UserMetadata save(UserMetadata user);

    //UserMetadata getUserMetadata(Long userId);

    UserMetadata loadUserMetadata(Long userId);

    User findById(Long id);

    User loadById(Long id);

    Set<User> findByIdIn(Set<Long> ids);

    User save(User user);

    //User getUser(Long userId);
}
