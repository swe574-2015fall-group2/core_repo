package com.boun.recommendation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.TaggedEntity.EntityType;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.GroupMemberRepository;
import com.boun.service.TagService;

import lombok.Data;

@Service
public class RecommendationEngine implements RecommendationService{
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	public Collection<RecommendationData> findRecommendedGroups(User user, List<Group> groupList){
		
		List<String> tagList = new ArrayList<String>();
		
		for (Group group : groupList) {
			
			if(group.getTagList() == null || group.getTagList().isEmpty()){
				continue;
			}
			
			for (String tag : group.getTagList()) {
				if(!tagList.contains(tag)){
					tagList.add(tag);
				}
			}
		}
		
		Hashtable<String, RecommendationData> recommendedGroupList = new Hashtable<String, RecommendationData>();
		int depth = 5;
		for (String tag : tagList) {
			walkThrough(tag, null, recommendedGroupList, depth, groupList, new ArrayList<String>());
		}
		
		if(recommendedGroupList.size() == 0){
			//If there is no group found with tag relations, search over group memberships
			
			searchViaMembership(true, user, recommendedGroupList, new ArrayList<String>(), depth, groupList);
		}
		
		return filterRecommendationCollection(recommendedGroupList.values());
	}
	
	private void walkThrough(String tag, Group group, Hashtable<String, RecommendationData> recommendedGroupTable, int depth, List<Group> groupsOfUser, List<String> processedTags){
		
		if(processedTags.contains(tag)){
			return;
		}
		
		if(group != null){
			RecommendationData data = recommendedGroupTable.get(group.getId());
			if(data == null){
				data = new RecommendationData(group, 0);
			}
			data.increaseRank();
			
			recommendedGroupTable.put(group.getId(), data);	
		}
		
		if(depth <= 0){
			return;
		}
		
		--depth;
		
		processedTags.add(tag);
		
		List<TaggedEntity> taggedGroupList = tagService.findTaggedEntityList(tag, EntityType.GROUP);
		if(taggedGroupList == null || taggedGroupList.isEmpty()){
			return;
		}
		
		for (TaggedEntity taggedEntity : filterGroup(groupsOfUser, taggedGroupList)) {
			
			Group g = (Group) taggedEntity;
			if(g.getTagList() == null || g.getTagList().isEmpty()){
				continue;
			}
			
			for (String t : filterTags(processedTags, g.getTagList())) {
				walkThrough(t, g, recommendedGroupTable, depth, groupsOfUser, processedTags);
			}
		}
	}
	
	private List<String> filterTags(List<String> processedTags, List<String> tags){
		
		List<String> resultList = new ArrayList<String>();
		
		for (String t1 : tags) {
			
			boolean found = false;
			for (String t2 : processedTags) {
				if(t1.equalsIgnoreCase(t2)){
					found = true;
					break;
				}
			}
			
			if(!found){
				resultList.add(t1);
			}
		}
		return resultList;
		
	}
	private List<TaggedEntity> filterGroup(List<Group> groupsOfUser, List<TaggedEntity> groupList){
		
		if(groupList == null || groupList.isEmpty()){
			return null;
		}
		
		if(groupsOfUser == null || groupsOfUser.isEmpty()){
			return null;
		}
		List<TaggedEntity> resultList = new ArrayList<TaggedEntity>();
		
		for (TaggedEntity g1 : groupList) {
			
			boolean found = false;
			for (TaggedEntity g2 : groupsOfUser) {
				if(g1.getId().equalsIgnoreCase(g2.getId())){
					found = true;
					break;
				}
			}
			
			if(!found){
				resultList.add(g1);
			}
		}
		return resultList;
	}
	
	@Data
	public static final class RecommendationData{
		private Group group;
		private int rank;
		
		public RecommendationData(Group group, int rank){
			this.group = group;
			this.rank = rank;
		}
		
		public void increaseRank(){
			++rank;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			
			RecommendationData d = (RecommendationData)o;
			
			return (d.getGroup().getId().equalsIgnoreCase(getGroup().getId()));
		}
		
		public int hashCode() {
			int code = 7;
			return code = 89 * code * getGroup().getId().hashCode();
		}
	}
	
	private void searchViaMembership(boolean first, User user, Hashtable<String, RecommendationData> recommendedGroupTable, List<String> processedUserList, int depth, List<Group> groupListOfAuthenticatedUser){
		
		if(processedUserList.contains(user.getId())){
			return;
		}
		
		if(depth <= 0){
			return;
		}
		--depth;
		
		processedUserList.add(user.getId());
		
		List<Group> groupList = groupMemberRepository.findGroupsOfUser(user.getId());
		for (Group group : groupList) {
			
			if(!first && group != null && !groupListOfAuthenticatedUser.contains(group)){
				RecommendationData data = recommendedGroupTable.get(group.getId());
				if(data == null){
					data = new RecommendationData(group, 0);
				}
				data.increaseRank();
				
				recommendedGroupTable.put(group.getId(), data);	
			}
			
			List<User> userList = groupMemberRepository.findUsersOfGroup(group.getId());
			
			for (User u : userList) {
				
				if(u.getId().equalsIgnoreCase(user.getId())){
					continue;
				}
				
				if(processedUserList.contains(u.getId())){
					continue;
				}
				
				searchViaMembership(false, u, recommendedGroupTable, processedUserList, depth, groupListOfAuthenticatedUser);
			}
		}
	}
	
	private List<RecommendationData> filterRecommendationCollection(Collection<RecommendationData> list){
		
		if(list == null || list.isEmpty()){
			return new ArrayList<RecommendationData>();
		}
		
		ArrayList<RecommendationData> flist = new ArrayList<RecommendationData>(list);
		Collections.sort(flist, new RecommendationDataSort());
		
		return flist.subList(0, flist.size() > 5 ? 5 : flist.size());
	}
	
	private static class RecommendationDataSort implements Comparator<RecommendationData> {

	    @Override
	    public int compare(RecommendationData o1, RecommendationData o2) {
	    	
	    	return (o1.getRank() < o2.getRank()) ? -1 :1;
	    }
	}
}
