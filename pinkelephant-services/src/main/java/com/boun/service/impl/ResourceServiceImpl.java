package com.boun.service.impl;

import com.boun.data.mongo.model.Resource;
import com.boun.data.mongo.model.Role;
import com.boun.data.mongo.repository.ResourceRepository;
import com.boun.service.PinkElephantService;
import com.boun.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ResourceServiceImpl extends PinkElephantService implements ResourceService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//TODO add this to a property file
	private final String RESOURCE_FILES_PATH = "resourcefiles/";

	@Autowired
	private ResourceRepository resourceRepository;


	@Override
	public Resource uploadResource(byte[] bytes, String name) {

		Resource resource = new Resource();
		resource.setName(name);

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

	public boolean delete(String id) {

		//TODO check if this resource is related to an object, then throw exception
		Resource resource = resourceRepository.findOne(id);

		File file = new File(RESOURCE_FILES_PATH + resource.getId());
		file.delete();

		resourceRepository.delete(resource);

		return true;
	}
}
