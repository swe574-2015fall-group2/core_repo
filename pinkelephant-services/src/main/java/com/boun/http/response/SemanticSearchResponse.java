package com.boun.http.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.boun.data.common.Constants;
import com.boun.data.mongo.model.TaggedEntity;
import com.boun.http.request.TagData;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SemanticSearchResponse {
	
	private List<SearchDetail> resultList;
	private List<String> clazzList;
	
	public void addDetail(TaggedEntity.EntityType type, String id, String description, TagData tag, float rank, boolean increaseCoOccuranceRank){
		if(resultList == null){
			resultList = new ArrayList<SearchDetail>();
		}
		SearchDetail detail = new SearchDetail(type, id, description, tag, rank);
		if(!resultList.contains(detail)){
			resultList.add(detail);	
		}else if(increaseCoOccuranceRank){
			resultList.remove(detail);
			detail.setRank(rank + Constants.SEMANTIC_CO_OCCURANCE_FACTOR);
			resultList.add(detail);	
		}
		
		if(tag.getClazz() != null){
			if(clazzList == null){
				clazzList = new ArrayList<String>();
			}
			
			if(!clazzList.contains(tag.getClazz())){
				clazzList.add(tag.getClazz());
			}
		}
	}
	
	public List<SearchDetail> getResultList(){
		Collections.sort(resultList, new SearchDetailSort());
		return resultList;
	}
	
	@Data
	private static class SearchDetail{
		private TaggedEntity.EntityType type;
		
		private String id;
		private String description;
		private TagData tag;
		
		private float rank;
		
		public SearchDetail(TaggedEntity.EntityType type, String id, String description, TagData tag, float rank){
			this.type = type;
			this.id = id;
			this.description = description;
			this.rank = rank;
			this.tag = tag;
		}
		
		public boolean equals(Object o){
			if(o == null){
				return false;
			}
			SearchDetail idx = (SearchDetail)o;
			
			return (idx.getId().equalsIgnoreCase(this.getId())) && (idx.getType() == this.getType());
		}
		
		@Override
		public int hashCode() {
			int code = 7;
			code = 89 * code * this.getTag().hashCode();
			code = code * this.getType().hashCode();
			return code;
		}
	}
	
	private static class SearchDetailSort implements Comparator<SearchDetail> {

	    @Override
	    public int compare(SearchDetail o1, SearchDetail o2) {
	    	
	    	return (o1.getRank() >= o2.getRank()) ? -1 : 1;
	    }
	}
}
