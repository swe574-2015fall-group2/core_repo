package com.boun.recommendation;

import java.util.Collection;
import java.util.List;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.User;
import com.boun.recommendation.RecommendationEngine.RecommendationData;

public interface RecommendationService {

	Collection<RecommendationData> findRecommendedGroups(User user, List<Group> groupList);
}
