package com.boun.dbpedia;

import java.util.List;

import org.dbpedia.lookup.ArrayOfResult;
import org.dbpedia.lookup.ArrayOfResult.Result.Classes;

import com.boun.data.dbpedia.OWLClassHierarchy;
import com.boun.data.dbpedia.OWLClassHierarchy.Node;
import com.boun.data.http.HTTPClient;
import com.boun.http.response.QueryLabelResponse;

public class HTTPQueryRunner {

	private static HTTPQueryRunner instance = new HTTPQueryRunner();
	
	private static final String DBPEDIA_URL = "http://lookup.dbpedia.org/api/search.asmx/PrefixSearch?MaxHits=20&QueryString=";
	
	private HTTPQueryRunner(){
	}
	
	public static HTTPQueryRunner getInstance() {
		return instance;
	}
	
	public QueryLabelResponse runQuery(String queryString){
		QueryLabelResponse response = new QueryLabelResponse(queryString);
		
		ArrayOfResult arrayOfResult = HTTPClient.getInstance().get(DBPEDIA_URL, queryString);
		
		List<ArrayOfResult.Result> resultList = arrayOfResult.getResult();
		for (ArrayOfResult.Result result : resultList) {
			
			Classes classes = result.getClasses();
			List<org.dbpedia.lookup.ArrayOfResult.Result.Classes.Class> classList = classes.getClazz();

			Node current = null;
			for (org.dbpedia.lookup.ArrayOfResult.Result.Classes.Class clazz : classList) {
				
				Node node = OWLClassHierarchy.getInstance().getHierarchy().get(clazz.getURI());
				
				if(node == null){
					continue;
				}
				
				if(current == null){
					current = node;
					continue;
				}
				
				int level = OWLClassHierarchy.getInstance().isChild(node.getUri(), current.getUri(), 0);
				if(level != 0){
					current = node;
				}
			}
			if(current == null){
				response.addData(result.getLabel(), null, result.getDescription());	
			}else{
				response.addData(result.getLabel(), current.getLabel(), result.getDescription());
			}
			
		}
		
		return response;
	}
}
