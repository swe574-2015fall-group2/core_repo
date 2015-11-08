package com.boun.data.mongo.repository.custom;

import com.boun.data.mongo.model.GroupMember;

public interface GroupMemberRepositoryCustom {

	GroupMember findGroupMember(String userId, String groupId);
}
