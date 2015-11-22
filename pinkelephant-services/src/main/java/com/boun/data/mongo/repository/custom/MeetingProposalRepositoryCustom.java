package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.MeetingProposal;

public interface MeetingProposalRepositoryCustom {

	public List<MeetingProposal> findMeetingProposals(String discussionId);

}
