package com.boun.service.impl;

import com.boun.app.client.PinkElephantHttpClient;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.util.ObjectUtils;
import com.boun.data.common.Status;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserMetadata;
import com.boun.data.mongo.repository.UserMetadataRepository;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.http.request.CreateProfileRequest;
import com.boun.service.PinkElephantService;
import com.boun.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl extends PinkElephantService implements UserService {

    @Autowired private PinkElephantHttpClient pinkElephantHttpClient;

    @Autowired private UserRepository userRepository;

    @Autowired private UserMetadataRepository userMetadataRepository;


    @Override
    public User createProfile(CreateProfileRequest createProfile, Long userId) {
        User user = userRepository.loadById(userId);
        //user = createProfile.to(user);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        save(UserMetadata.from(user));
        return user;
    }



    @Override
    public UserMetadata save(UserMetadata user) {
        userMetadataRepository.save(user);
        //indexService.index(user);
        //cacheRepository.putUser(user);
        return user;
    }

    @Override
    public List<User> save(Collection<User> users) {
        List<User> list = userRepository.save(users);
        saveInOther(list);
        return list;
    }


    private void saveInOther(Collection<User> users) {
        List<UserMetadata> userMetadataList = new ArrayList<UserMetadata>();
        for(User user : users){
          userMetadataList.add(UserMetadata.from(user));
        }
        userMetadataRepository.save(userMetadataList);
        //indexService.bulkUpdate(userMetadataList);
        //cacheRepository.putUsers(userMetadataList);
    }

    @Override
    public UserMetadata loadUserMetadata(Long userId) {
        UserMetadata user = null;//getUserMetadata(userId);
        if(ObjectUtils.isNull(user)) {
            throw new PinkElephantRuntimeException(404, "200", "Not Found","User["+userId+"] not found");
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User loadById(Long id) {
        return userRepository.loadById(id);
    }

    @Override
    public Set<User> findByIdIn(Set<Long> ids) {
        return userRepository.findByIdIn(ids);
    }

    @Override
    public User save(User user) {
        User savedUser = userRepository.save(user);
        UserMetadata userMetadata = UserMetadata.from(savedUser);
        userMetadataRepository.save(userMetadata);

        return savedUser;
    }


}
