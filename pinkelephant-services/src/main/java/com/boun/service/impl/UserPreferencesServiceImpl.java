package com.boun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserPreferences;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.service.UserPreferencesService;
import com.boun.service.UserService;

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
        userRepository.save(user);

        return preferences;
    }
}
