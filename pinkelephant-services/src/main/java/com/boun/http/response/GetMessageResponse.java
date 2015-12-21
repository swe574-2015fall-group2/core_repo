package com.boun.http.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.boun.data.mongo.model.Messagebox.MessageDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetMessageResponse extends ActionResponse{

	private List<MessageObj> messages;
	private int unreadCount;
	
	public void addMessageDetails(String messageBoxId, String receiverId, String senderId, List<MessageDetails> msgDetails){
		if(messages == null){
			messages = new ArrayList<MessageObj>();
		}
		
		Collections.sort(msgDetails, new MessageSort());
		MessageObj msg = new MessageObj(messageBoxId, receiverId, senderId, msgDetails);
		
		messages.add(msg);
		unreadCount += msg.getUnreadCount();
	}
	
	@Data
	private static class MessageObj{
		private String receiverId;
		private String senderId;
		private String messageBoxId;
		private List<MessageDetails> messageList;

		@JsonIgnore
		private int unreadCount;
		
		public MessageObj(String messageBoxId, String receiverId, String senderId, List<MessageDetails> messageList){
			this.receiverId = receiverId;
			this.messageList = messageList;
			this.senderId = senderId;
			this.messageBoxId = messageBoxId;
			
			for (MessageDetails messageDetails : messageList) {
				if(!messageDetails.isRead()){
					++this.unreadCount;
				}
			}
		}
	}
	
	private static class MessageSort implements Comparator<MessageDetails> {

	    @Override
	    public int compare(MessageDetails o1, MessageDetails o2) {
	    	if(o1.getDatetime() == null || o2.getDatetime() == null){
	    		return 1;
	    	}
	    	
	    	return (o1.getDatetime().before(o2.getDatetime())) ? -1 : 1;
	    }
	}
}


