package com.boun.service;

import java.util.List;

import com.boun.data.mongo.model.Messagebox;
import com.boun.http.request.BaseRequest;
import com.boun.http.request.MessageReadRequest;
import com.boun.http.request.SendMessageRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.GetMessageResponse;

public interface MessageboxService {

	public ActionResponse sendMessage(SendMessageRequest request);
	
	public GetMessageResponse getAllMessagesBySender(BaseRequest request);
	
	public GetMessageResponse getAllMessagesByReceiver(BaseRequest request);
	
	public Messagebox findMessageBox(String receiverId, String senderId);
	
	public List<Messagebox> findMessageBoxByReceiver(String receiverId);
	
	public List<Messagebox> findMessageBoxBySender(String senderId);
	
	public ActionResponse read(MessageReadRequest request);
	
	public ActionResponse unread(MessageReadRequest request);
}
