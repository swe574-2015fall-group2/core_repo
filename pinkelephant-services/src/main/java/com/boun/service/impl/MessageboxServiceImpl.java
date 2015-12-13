package com.boun.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.Messagebox;
import com.boun.data.mongo.model.Messagebox.MessageDetails;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.repository.MessageboxRepository;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.MessageReadRequest;
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
	private MessageboxRepository msgBoxRepository;
	
	@Autowired
	private UserService userService;

	public Messagebox findById(String id){
		Messagebox messagebox = msgBoxRepository.findOne(id);
		if(messagebox == null){
			throw new PinkElephantRuntimeException(400, ErrorCode.MESSAGE_NOT_FOUND, "");
		}
		return messagebox;
	}
	
	@Override
	public Messagebox findMessageBox(String senderId, String receiverId) {
		Messagebox messageBox = msgBoxRepository.search(senderId, receiverId);
		return messageBox;
	}
	
	@Override
	public List<Messagebox> findMessageBoxBySender(String senderId) {
		List<Messagebox> messageBoxList = msgBoxRepository.searchBySender(senderId);
		return messageBoxList;
	}
	
	@Override
	public List<Messagebox> findMessageBoxByReceiver(String receiverId) {
		List<Messagebox> messageBoxList = msgBoxRepository.searchByReceiver(receiverId);
		return messageBoxList;
	}
	
	public ActionResponse read(MessageReadRequest request){
		ActionResponse response = new ActionResponse();
		
		read(request.getMessageBoxId(), request.getMessageId(), true);
		
		response.setAcknowledge(true);
		return response;
	}
	
	public ActionResponse unread(MessageReadRequest request){
		ActionResponse response = new ActionResponse();
		
		read(request.getMessageBoxId(), request.getMessageId(), false);
		
		response.setAcknowledge(true);
		return response;
	}
	
	private void read(String messageboxId, String messageId, boolean read){
		Messagebox messagebox = findById(messageboxId);
		List<MessageDetails> messages = messagebox.getMessages();
		
		if(messages == null || messages.isEmpty()){
			throw new PinkElephantRuntimeException(400, ErrorCode.MESSAGE_NOT_FOUND, "");
		}
		
		for (MessageDetails messageDetails : messages) {
			if(messageDetails.getId().equalsIgnoreCase(messageId)){
				messageDetails.setRead(read);
				break;
			}
		}
		
		msgBoxRepository.save(messagebox);
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
		msgBoxRepository.save(messageBox);
		
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
