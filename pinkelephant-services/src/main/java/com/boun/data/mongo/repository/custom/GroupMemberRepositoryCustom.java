package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupCount;
import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.model.User;

public interface GroupMemberRepositoryCustom {

	GroupMember findGroupMember(String userId, String groupId);
	
	List<Group> findGroupsOfUser(String userId);
	
	List<User> findUsersOfGroup(String groupId);

	List<GroupCount> findPopularGroups();
}
