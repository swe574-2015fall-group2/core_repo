package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.Meeting;

public interface MeetingRepositoryCustom {

	public List<Meeting> findMeetings(String groupId);
}