package com.boun.data.mongo.repository;

import com.boun.data.mongo.model.UserMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface UserMetadataRepository extends MongoRepository<UserMetadata, Long>, UserMetadataRepositoryCustom {

    UserMetadata findById(Long userId);

    UserMetadata findByUsername(String username);

    List<UserMetadata> findByIdIn(Collection<Long> ids);


}
