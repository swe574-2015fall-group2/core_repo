package com.boun.http.response;

import java.util.ArrayList;
import java.util.List;

import com.boun.data.mongo.model.TaggedEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SemanticSearchResponse {
	
	private List<SearchDetail> resultList;
	
	public void addDetail(TaggedEntity.EntityType type, String id, String description, int priority){
		if(resultList == null){
			resultList = new ArrayList<SearchDetail>();
		}
		resultList.add(new SearchDetail(type, id, description, priority));
	}
	
	@Data
	private static class SearchDetail{
		private TaggedEntity.EntityType type;
		
		private String id;
		private String description;
		
		private int priority;
		
		public SearchDetail(TaggedEntity.EntityType type, String id, String description, int priority){
			this.type = type;
			this.id = id;
			this.description = description;
			this.priority = priority;
		}
	}
}
