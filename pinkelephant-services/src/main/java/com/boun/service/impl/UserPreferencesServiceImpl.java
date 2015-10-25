package com.boun.service.impl;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserMetadata;
import com.boun.data.mongo.model.UserPreferences;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.service.UserPreferencesService;
import com.boun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferencesServiceImpl implements UserPreferencesService {

    @Autowired private UserRepository userRepository;

    @Autowired
    UserService userService;

    @Override
    public UserPreferences getPreferences(Long id) {
        User user = userRepository.loadById(id);
        return user.getPreferences();
    }

    @Override
    public UserPreferences save(Long id, UserPreferences preferences) {
        User user = userRepository.loadById(id);
        user.setPreferences(preferences);
        userService.save(user);

        return preferences;
    }
}
