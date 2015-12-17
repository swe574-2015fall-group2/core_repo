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
	
	public void addData(String label, String clazz){
		if(dataList == null){
			dataList = new ArrayList<DataObj>();
		}
		dataList.add(new DataObj(label, clazz));
	}
	
	@Data
	private static class DataObj{
		private String label;
		private String clazz;
		
		private DataObj(String label, String clazz){
			this.label = label;
			this.clazz = clazz;
		}
	}
}
