package com.boun.service.impl;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.common.enums.ResourceType;
import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.repository.ResourceRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.CreateResourceRequest;
import com.boun.http.request.DeleteResourceRequest;
import com.boun.service.PinkElephantService;
import com.boun.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;

@Service
public class ResourceServiceImpl extends PinkElephantService implements ResourceService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//TODO add this to a property file
	private final String RESOURCE_FILES_PATH = "resourcefiles/";

	@Autowired
	private ResourceRepository resourceRepository;

	@Override
	public Resource findById(String resourceId) {
		Resource resource = resourceRepository.findOne(resourceId);

		if(resource == null) {
			throw new PinkElephantRuntimeException(400, ErrorCode.RESOURCE_NOT_FOUND, "");
		}

		return resource;
	}

	@Override
	public Resource createExternalResource(CreateResourceRequest request) {

		validate(request);

		Resource resource = new Resource();
		resource.setName(request.getName());
		resource.setLink(request.getLink());
		resource.setType(ResourceType.EXTERNAL);
		resource.setCreatedAt(new Date());
		resource.setCreator(PinkElephantSession.getInstance().getUser(request.getAuthToken()));

		resource = resourceRepository.save(resource);

		return  resource;

	}

	@Override
	public Resource uploadResource(byte[] bytes, String name, String authToken) {

		validate(authToken);

		Resource resource = new Resource();
		resource.setName(name);
		resource.setType(ResourceType.INTERNAL);
		//TODO resource.setLink(request.getLink());
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

	public boolean delete(DeleteResourceRequest request) {

		validate(request);

		//TODO check if this resource is related to an object, then throw exception
		Resource resource = findById(request.getId());

		//TODO seperate service or function for files?
		File file = new File(RESOURCE_FILES_PATH + resource.getId());
		file.delete();

		resourceRepository.delete(resource);

		return true;
	}
}
