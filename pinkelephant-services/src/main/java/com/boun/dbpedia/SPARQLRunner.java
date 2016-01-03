package com.boun.dbpedia;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.boun.data.cache.DBPediaCache;
import com.boun.data.dbpedia.OWLClassHierarchy;
import com.boun.data.dbpedia.OWLClassHierarchy.Node;
import com.boun.http.response.QueryLabelResponse;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class SPARQLRunner {

	private static SPARQLRunner instance = new SPARQLRunner();
	
	private String DBPEDIA_QUERY ="PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
									"PREFIX  rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
									"PREFIX dbpedia-owl:<http://dbpedia.org/ontology/>\n\n" +
										"select ?label ?type where {\n" +
										"?s rdfs:label ?label.\n" +
										"?s rdf:type ?type.\n" +
										"FILTER langMatches( lang(?label), 'EN' ).\n"+
										"?label <bif:contains> \"'%s'\" .\n" +
									"} LIMIT 1500";
	
	
	private SPARQLRunner(){
	}
	
	public static SPARQLRunner getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		getInstance().runQuery("javascript");
	}
	
	public QueryLabelResponse runQuery(String queryString) {

		QueryLabelResponse response = DBPediaCache.getInstance().get(queryString);
		if(response != null){
			return response;
		}
		response = new QueryLabelResponse(queryString);
		
		String dbpeaidSparqlQueryString = String.format(DBPEDIA_QUERY, queryString);
		
        Query query = QueryFactory.create(dbpeaidSparqlQueryString);
        QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query );
        ResultSet results = qExe.execSelect();
        
        Hashtable<String, List<String>> resultTable = new Hashtable<String, List<String>>();
        
        while(results.hasNext()){
        	QuerySolution qs = results.next();
        	
        	String label = qs.get("label").asLiteral().getString();
        	String type = qs.get("type").toString();
        	
        	List<String> typeList = resultTable.get(label);
        	if(typeList == null){
        		typeList = new ArrayList<String>();
        	}
        	typeList.add(type);
        	
        	resultTable.put(label, typeList);
        }
        
        for (String label : resultTable.keySet()) {
        	Node current = null;
        	
        	List<String> typeList = resultTable.get(label);
        	for (String type : typeList) {
				
        		if(type.equalsIgnoreCase("http://www.w3.org/2002/07/owl#Thing")){
        			continue;
        		}
        		
        		Node node = OWLClassHierarchy.getInstance().getHierarchy().get(type);
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
        	
        	//TODO load umbel, wikidata and skos classes

        	if(current != null){
	        	response.addData(label, current.getLabel(), null);
	        }
		}
        
        DBPediaCache.getInstance().put(queryString, response);
        
        return response;
    }
}