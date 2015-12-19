package com.boun.http.response;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryLabelResponse extends ActionResponse{

	private List<DataObj> dataList;
	
	public void addData(String label, String clazz, String description){
		if(dataList == null){
			dataList = new ArrayList<DataObj>();
		}
		dataList.add(new DataObj(label, clazz, description));
	}
	
	@Data
	private static class DataObj{
		private String label;
		private String clazz;
		private String description;
		
		private DataObj(String label, String clazz, String description){
			this.label = label;
			this.clazz = clazz;
//			this.description = description; 
		}
	}
}
