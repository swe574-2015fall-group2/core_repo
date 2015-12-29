package com.boun.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.enums.ResourceType;
import com.boun.data.mongo.model.EntityRelation;
import com.boun.data.mongo.model.Group;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.data.mongo.repository.EntityRelationRepository;
import com.boun.data.mongo.repository.ResourceRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BasicDeleteRequest;
import com.boun.http.request.BasicQueryRequest;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.response.ResourceResponse;
import com.boun.service.DiscussionService;
import com.boun.service.GroupService;
import com.boun.service.MeetingService;
import com.boun.service.PinkElephantTaggedService;
import com.boun.service.ResourceService;
import com.boun.service.TagService;

@Service
public class ResourceServiceImpl extends PinkElephantTaggedService implements ResourceService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//TODO add this to a property file
	private final String RESOURCE_FILES_PATH = "resourcefiles/";

	@Autowired
	private ResourceRepository resourceRepository;

	@Autowired
	private GroupService groupService;
	
	@Autowired
	private MeetingService meetingService;
	
	@Autowired
	private DiscussionService discussionService;
	
	@Autowired
	private EntityRelationRepository entityRelationRepository;

	@Autowired
	private TagService tagService;
	
	@Override
	public void save(TaggedEntity entity) {
		resourceRepository.save((Resource)entity);
	}
	
	@Override
	public Resource findById(String resourceId) {
		Resource resource = resourceRepository.findOne(resourceId);

		if(resource == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.RESOURCE_NOT_FOUND, "");
		}

		return resource;
	}

	@Override
	public List<Resource> findByIds(List<String> resourceIds) {
		List<Resource> resources = (List<Resource>)resourceRepository.findAll(resourceIds);

		/*TODO temporarily commented.
		if(resources == null || resources.isEmpty() || resources.size() != resourceIds.size()) {
			throw new PinkElephantRuntimeException(400, ErrorCode.RESOURCE_NOT_FOUND, "");
		}
		*/
		return resources;
	}


	@Override
	public Resource createExternalResource(CreateResourceRequest request) {

		validate(request);

		Group group = groupService.findById(request.getGroupId());

		Resource resource = new Resource();
		resource.setDescription(request.getName());
		resource.setLink(request.getLink());
		resource.setType(ResourceType.EXTERNAL);
		resource.setGroup(group);
		resource.setCreatedAt(new Date());
		resource.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));
		resource.setTagList(request.getTagList());
		
		resource = resourceRepository.save(resource);
		tagService.tag(request.getTagList(), resource, true);

		return  resource;

	}

	public ResourceResponse downloadResource(String resourceId) throws FileNotFoundException {

		ResourceResponse response = new ResourceResponse();

		Resource resource = resourceRepository.findOne(resourceId);

		response.setFile(new File(RESOURCE_FILES_PATH + resource.getId()));
		response.setFileName(resource.getDescription());
		return response;

	}

	@Override
	public Resource uploadResource(byte[] bytes, String name, String groupId, String authToken) {

		validate(authToken);

		Group group = groupService.findById(groupId);

		Resource resource = new Resource();
		resource.setDescription(name);
		resource.setType(ResourceType.INTERNAL);
		//TODO resource.setLink(request.getLink());
		resource.setGroup(group);
		resource.setCreatedAt(new Date());
		resource.setCreator(PinkElephantSession.getInstance().getUser(authToken));
		resource = resourceRepository.save(resource);

		BufferedOutputStream stream = null;
		try {
			File directory = new File(RESOURCE_FILES_PATH);
			if(!directory.exists())
				directory.mkdir();

			stream = new BufferedOutputStream(new FileOutputStream(new File(RESOURCE_FILES_PATH + resource.getId())));
			stream.write(bytes);
			stream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resource;
	}

	public boolean delete(BasicDeleteRequest request) {

		validate(request);

		//TODO check if this resource is related to an object, then throw exception
		Resource resource = findById(request.getId());

		//TODO seperate service or function for files?
		File file = new File(RESOURCE_FILES_PATH + resource.getId());
		file.delete();

		resourceRepository.delete(resource);

		return true;
	}

	public List<Resource> queryResourcesOfGroup(BasicQueryRequest request) {
		Group group = groupService.findById(request.getId());
		return resourceRepository.findResources(group.getId());
	}
	
	@Override
	protected TagService getTagService() {
		return tagService;
	}

	@Override
	protected ResourceService getResourceService() {
		return this;
	}

	@Override
	protected MeetingService getMeetingService() {
		return meetingService;
	}

	@Override
	protected DiscussionService getDiscussionService() {
		return discussionService;
	}

	@Override
	protected EntityRelationRepository getEntityRelationRepository() {
		return entityRelationRepository;
	}

	@Override
	public List<EntityRelation> findRelationById(String meetindId) {
		return entityRelationRepository.findRelationByResourceId(meetindId);
	}
}
