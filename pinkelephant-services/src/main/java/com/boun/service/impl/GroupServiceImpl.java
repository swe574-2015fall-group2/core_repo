package com.boun.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.enums.GroupStatus;
import com.boun.data.common.enums.MemberStatus;
import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.GroupCount;
import com.boun.data.mongo.model.GroupMember;
import com.boun.data.mongo.model.ImageInfo;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.EntityRelationRepository;
import com.boun.data.mongo.repository.GroupMemberRepository;
import com.boun.data.mongo.repository.GroupRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateUpdateGroupRequest;
import com.boun.http.request.JoinLeaveGroupRequest;
import com.boun.http.request.SetRolesRequest;
import com.boun.http.request.UploadImageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.CreateResponse;
import com.boun.http.response.GetGroupResponse;
import com.boun.http.response.ListGroupResponse;
import com.boun.recommendation.RecommendationEngine.RecommendationData;
import com.boun.recommendation.RecommendationService;
import com.boun.service.DiscussionService;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.PinkElephantTaggedService;
import com.boun.service.ResourceService;
import com.boun.service.TagService;
import com.boun.service.UserService;
import com.boun.util.ImageUtil;

@Service
public class GroupServiceImpl extends PinkElephantTaggedService implements GroupService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired
	private GroupMemberRepository groupMemberRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private TagService tagService;
	
	@Autowired
	private RecommendationService recommendationService;
	
	@Override
	public Group findById(String groupId) {
		Group group = groupRepository.findOne(groupId);

		if(group == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		return group;
	}

	@Override
	public Group findByName(String groupName) {
		Group group = groupRepository.findByName(groupName);

		if(group == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		return group;
	}
	
	@Override
	public void save(TaggedEntity entity) {
		groupRepository.save((Group) entity);
	}

	public void delete(Group entity) {
		groupRepository.delete(entity);
	}

	@Override
	public boolean archiveGroup(BasicDeleteRequest request) {
		Group group = groupRepository.findOne(request.getId());

		delete(group);

		return true;
	}

	@Override
	public CreateResponse createGroup(CreateUpdateGroupRequest request) {
		
		validate(request);
		
		CreateResponse response = new CreateResponse();
		if(request.getName() == null || "".equalsIgnoreCase(request.getName())){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "Group name is empty", "");
		}
		
		Group group = groupRepository.findByName(request.getName());
		if(group != null){
			throw new PinkElephantRuntimeException(400, ErrorCode.DUPLICATE_GROUP, "");
		}

		group = new Group();
		group.setName(request.getName());
		group.setDescription(request.getDescription());
		group.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		group.setCreatedAt(new Date());
		group.setStatus(GroupStatus.ACTIVE);
		group.setTagList(request.getTagList());
		
		group = groupRepository.save(group);
		
		response.setAcknowledge(true);
		response.setEntityId(group.getId());

		tagService.tag(request.getTagList(), group, true);	

		JoinLeaveGroupRequest joinLeaveGroupRequest = new JoinLeaveGroupRequest();
		joinLeaveGroupRequest.setAuthToken(request.getAuthToken());
		joinLeaveGroupRequest.setGroupId(group.getId());

		joinGroup(joinLeaveGroupRequest);
		
		
		return response;
	}
	
	@Override
	public ActionResponse uploadImage(UploadImageRequest request){
		
		validate(request);
		
		Group group = findById(request.getEntityId());
		
		String imagePath = ImageUtil.saveImage("User", request);

		ImageInfo imageInfo = new ImageInfo();
		imageInfo.setImagePath(imagePath);
		imageInfo.setType(request.getFileType());
		
		group.setImage(imageInfo);
		groupRepository.save(group);
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		return response;
	}
	
	@Override
	public ActionResponse updateGroup(CreateUpdateGroupRequest request) {

		validate(request);

		ActionResponse response = new ActionResponse();
		Group group = findById(request.getGroupId());
		group.setDescription(request.getDescription());
		group.setName(request.getName());
		
		updateTag(group, request.getTagList());
		
		groupRepository.save(group);
		
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ActionResponse joinGroup(JoinLeaveGroupRequest request) {

		validate(request);

		ActionResponse response = new ActionResponse();
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());

		Group group = findById(request.getGroupId());

		User user = userService.findById(authenticatedUser.getId());

		GroupMember groupMember = groupMemberRepository.findGroupMember(user.getId(), group.getId());
		if(groupMember != null){

			if(groupMember.getStatus().value() == MemberStatus.ACTIVE.value()){
				throw new PinkElephantRuntimeException(400, ErrorCode.USER_IS_ALREADY_A_MEMBER, "");
			}
			
			if(groupMember.getStatus().value() == MemberStatus.BLOCKED.value()){
				throw new PinkElephantRuntimeException(400, ErrorCode.USER_IS_BLOCKED, "");
			}
			
			groupMember.setStatus(MemberStatus.ACTIVE);
		}else{
			groupMember = new GroupMember();
			groupMember.setGroup(group);
			groupMember.setUser(user);
			groupMember.setStatus(MemberStatus.ACTIVE);
		}

		groupMemberRepository.save(groupMember);
		response.setAcknowledge(true);


		//TODO delete!!! temporary role setter
		List<String> roles = new ArrayList<>();

		// 56699c72d4c652c6f9e2ab47 local
		// 56844e2fe4b0d46054a91c47 remote
		roles.add("56844e2fe4b0d46054a91c47");  // buraya static bir roleID'si eklenecek,

		SetRolesRequest setRolesRequest = new SetRolesRequest();
		setRolesRequest.setGroupId(group.getId());
		setRolesRequest.setAuthToken(request.getAuthToken());
		setRolesRequest.setRoleIds(roles);
		setRolesRequest.setUserId(authenticatedUser.getId());

		userService.setRoles(setRolesRequest);
		//

		return response;
	}

	@Override
	public ActionResponse leaveGroup(JoinLeaveGroupRequest request) {

		validate(request);

		ActionResponse response = new ActionResponse();
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());

		Group group = findById(request.getGroupId());

		GroupMember groupMember = groupMemberRepository.findGroupMember(authenticatedUser.getId(), request.getGroupId());
		if(groupMember == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.USER_IS_NOT_A_MEMBER, "");
		}
		
		groupMemberRepository.delete(groupMember);
		response.setAcknowledge(true);

		return response;
	}
	
	@Override
	public ListGroupResponse getMyGroups(BaseRequest request) {
		
		validate(request);
		
		ListGroupResponse response = new ListGroupResponse();
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());

		List<Group> groupList = findGroupsOfUser(authenticatedUser.getId());

		for (Group group : groupList) {
			response.addGroup(group.getId(), group.getName(), group.getDescription(), true, group.getTagList());
		}

		response.setAcknowledge(true);

		return response;
	}

	@Override
	public List<Group> findGroupsOfUser(String userId) {

		List<Group> groupList = groupMemberRepository.findGroupsOfUser(userId);

		if(groupList == null || groupList.isEmpty()){
			//throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
			groupList = new ArrayList<Group>();
		}

		return groupList;
	}

	@Override
	public GetGroupResponse queryGroup(BasicQueryRequest request) {
		
		validate(request);
		
		Group group = findById(request.getId());
		
		GetGroupResponse response = new GetGroupResponse();
		response.setDescription(group.getDescription());
		response.setId(group.getId());
		response.setName(group.getName());
		response.setImage(ImageUtil.getImage(group.getImage()));			
		response.setTagList(group.getTagList());

		List<User> usersOfGroup = groupMemberRepository.findUsersOfGroup(group.getId());
		response.mapUsers(usersOfGroup);

		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ListGroupResponse getAllGroups(BaseRequest request) {
		
		validate(request);
		
		//TODO group all groups as popular, my groups, others etc..
		
		ListGroupResponse response = new ListGroupResponse();
		List<Group> groupList = groupRepository.findAll();
		if(groupList == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}

		ListGroupResponse myGroupsResponse = getMyGroups(request);
		List<ListGroupResponse.GroupObj> myGroupList = myGroupsResponse.getGroupList();

		for (Group group : groupList) {
			if(group.getStatus() == null || group.getStatus().value() != GroupStatus.ACTIVE.value()){
				continue;
			}
			response.addGroup(group.getId(), group.getName(), group.getDescription(), checkIfJoined(myGroupList, group), group.getTagList());
		}

		response.setAcknowledge(true);

		return response;
	}

	@Override
	public ListGroupResponse getLatestGroups(BaseRequest request) {

		validate(request);

		ListGroupResponse response = new ListGroupResponse();

		List<Group> groups = groupRepository.findLatestGroups();
		if(groups == null || groups.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}
		
		ListGroupResponse myGroupsResponse = getMyGroups(request);
		List<ListGroupResponse.GroupObj> myGroupList = myGroupsResponse.getGroupList();
		
		for (Group group : groups) {
			response.addGroup(group.getId(), group.getName(), group.getDescription(), checkIfJoined(myGroupList, group), group.getTagList());			
		}
		
		response.setAcknowledge(true);
		return response;
	}

	@Override
	public ListGroupResponse getPopularGroups(BaseRequest request) {

		validate(request);

		ListGroupResponse response = new ListGroupResponse();

		List<GroupCount> groups = groupMemberRepository.findPopularGroups();
		if(groups == null || groups.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.GROUP_NOT_FOUND, "");
		}
		
		ListGroupResponse myGroupsResponse = getMyGroups(request);
		List<ListGroupResponse.GroupObj> myGroupList = myGroupsResponse.getGroupList();
		
		for (GroupCount groupCount : groups) {
			response.addGroup(groupCount, checkIfJoined(myGroupList, groupCount.getGroup()));
		}
		
		return response;
	}
	
	public ListGroupResponse findRecommendedGroups(BaseRequest request){
		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());


		List<Group> groupList = findGroupsOfUser(authenticatedUser.getId());

		Collection<RecommendationData> recommendedGroupList = recommendationService.findRecommendedGroups(authenticatedUser, groupList);


		ListGroupResponse response = new ListGroupResponse();
		
		for (RecommendationData data : recommendedGroupList) {
			Group group = data.getGroup();
			response.addGroup(group.getId(), group.getName(), group.getDescription(), false, group.getTagList(), data.getRank());	
		}
		
		return response;
	}
	
	private Boolean checkIfJoined(List<ListGroupResponse.GroupObj> myGroups, Group group) {

		if(myGroups == null || myGroups.isEmpty()){
			return false;
		}
		for(ListGroupResponse.GroupObj myGroup: myGroups)
		{
			if(myGroup.getId().equals(group.getId()))
				return true;
		}
		return false;
	}


	
	@Override
	protected TagService getTagService() {
		return tagService;
	}

	@Override
	public List<Group> findAllGroupList() {
		return groupRepository.findAll();
	}

	@Override
	protected ResourceService getResourceService() {
		return null;
	}

	@Override
	protected MeetingService getMeetingService() {
		return null;
	}

	@Override
	protected DiscussionService getDiscussionService() {
		return null;
	}

	@Override
	protected EntityRelationRepository getEntityRelationRepository() {
		return null;
	}

	@Override
	public List<EntityRelation> findRelationById(String meetindId) {
		return null;
	}
}
