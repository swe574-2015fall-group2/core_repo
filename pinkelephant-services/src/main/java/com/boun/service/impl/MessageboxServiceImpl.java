package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Messagebox;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.MessageboxRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.SendMessageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetMessageResponse;
import com.boun.service.MessageboxService;
import com.boun.service.PinkElephantService;
import com.boun.service.UserService;

@Service
public class MessageboxServiceImpl extends PinkElephantService implements MessageboxService{

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MessageboxRepository tagRepository;
	
	@Autowired
	private UserService userService;

	@Override
	public Messagebox findMessageBox(String senderId, String receiverId) {
		Messagebox messageBox = tagRepository.search(senderId, receiverId);
		return messageBox;
	}
	
	@Override
	public List<Messagebox> findMessageBoxBySender(String senderId) {
		List<Messagebox> messageBoxList = tagRepository.searchBySender(senderId);
		return messageBoxList;
	}
	
	@Override
	public List<Messagebox> findMessageBoxByReceiver(String receiverId) {
		List<Messagebox> messageBoxList = tagRepository.searchByReceiver(receiverId);
		return messageBoxList;
	}
	
	@Override
	public ActionResponse sendMessage(SendMessageRequest request) {
		validate(request);
		
		User receiver = userService.findById(request.getReceiverId());
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		Messagebox messageBox = findMessageBox(authenticatedUser.getId(), request.getReceiverId());
		if(messageBox == null){
			messageBox = new Messagebox();
		}
		
		messageBox.setSender(authenticatedUser);
		messageBox.setReceiver(receiver);
		messageBox.addMessage(request.getMessage());
		
		ActionResponse response = new ActionResponse();
		response.setAcknowledge(true);
		tagRepository.save(messageBox);
		
		return response;
	}
	
	@Override
	public GetMessageResponse getAllMessagesBySender(BaseRequest request) {

		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		List<Messagebox> messageBoxList = findMessageBoxBySender(authenticatedUser.getId());
		if(messageBoxList == null || messageBoxList.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.MESSAGE_NOT_FOUND, "");
		}
		
		GetMessageResponse response = new GetMessageResponse();
		for (Messagebox mb : messageBoxList) {
			response.addMessageDetails(mb.getReceiver().getId(), mb.getMessages());	
		}
		
		return response;
	}
	
	@Override
	public GetMessageResponse getAllMessagesByReceiver(BaseRequest request) {

		validate(request);
		
		User authenticatedUser = PinkElephantSession.getInstance().getUser(request.getAuthToken());
		
		List<Messagebox> messageBoxList = findMessageBoxByReceiver(authenticatedUser.getId());
		if(messageBoxList == null || messageBoxList.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.MESSAGE_NOT_FOUND, "");
		}
		
		GetMessageResponse response = new GetMessageResponse();
		for (Messagebox mb : messageBoxList) {
			response.addMessageDetails(mb.getReceiver().getId(), mb.getMessages());	
		}
		
		return response;
	}
}
