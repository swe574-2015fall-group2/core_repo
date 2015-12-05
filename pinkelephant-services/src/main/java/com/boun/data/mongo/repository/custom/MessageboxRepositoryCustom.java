package com.boun.data.mongo.repository.custom;

import java.util.List;

import com.boun.data.mongo.model.Messagebox;

public interface MessageboxRepositoryCustom {

	public Messagebox search(String senderId, String receiverId);
	
	public List<Messagebox> searchBySender(String senderId);
	
	public List<Messagebox> searchByReceiver(String receiverId);
}
