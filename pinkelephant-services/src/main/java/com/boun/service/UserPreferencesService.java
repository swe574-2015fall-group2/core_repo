package com.boun.service;

import com.boun.data.mongo.model.UserPreferences;

public interface UserPreferencesService {

    UserPreferences getPreferences(Long id);

    UserPreferences save(Long id, UserPreferences preferences);

}
