package com.boun.http.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.boun.data.mongo.model.Note;
import com.boun.data.mongo.model.User;
import com.boun.http.request.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListNoteResponse {

	private List<NoteObj> noteList;
	
	public void addNote(Note note){
		if(noteList == null){
			noteList = new ArrayList<NoteObj>();
		}
		noteList.add(new NoteObj(note));
	}
	
	public List<NoteObj> getNoteList(){
		Collections.sort(noteList, new NoteObjSort());
		return noteList;
	}
	
	@Data
	public static class NoteObj {
		private String id;
		private String title;
		private String creatorId;
		private String groupId;
		private String meetingId;
		private Date createdAt;

		private Boolean isPinned;
		
		private List<TagData> tagList;
		
		public NoteObj(Note note) {
			this.id = note.getId();
			this.title = note.getTitle();
			this.createdAt = note.getCreatedAt();
			this.creatorId =note.getCreator().getId();
			this.groupId = note.getGroup() != null ? note.getGroup().getId():null;
			this.meetingId = note.getMeeting() != null? note.getMeeting().getId():null;
			this.isPinned = note.getIsPinned();
			this.tagList = note.getTagList();
		}
		
		private Set<String> getUsernameSet(Set<User> userSet){
			
			if(userSet == null || userSet.isEmpty()){
				return null;
			}
			
			Set<String> nameSet = new HashSet<String>();
			for (User user : userSet) {
				nameSet.add(user.getUsername());
			}
			return nameSet;
		}
	}
	
	private static class NoteObjSort implements Comparator<NoteObj> {

	    @Override
	    public int compare(NoteObj o1, NoteObj o2) {
	    	if(o1.getCreatedAt() == null || o2.getCreatedAt() == null){
	    		return 1;
	    	}
	    	
	    	return (o1.getCreatedAt().before(o2.getCreatedAt())) ? -1 : 1;
	    }
	}
}


