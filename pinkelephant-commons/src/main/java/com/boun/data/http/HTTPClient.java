package com.boun.data.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dbpedia.lookup.ArrayOfResult;

public class HTTPClient {

	private static HTTPClient instance = new HTTPClient();
	
	private HttpClient client;
	private Unmarshaller unmarshaller;
	
	private HTTPClient(){
		try{
			client = HttpClientBuilder.create().build();
			JAXBContext jaxbContext = JAXBContext.newInstance(ArrayOfResult.class);
			unmarshaller = jaxbContext.createUnmarshaller();	
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	
	public static HTTPClient getInstance() {
		return instance;
	}
	
	public ArrayOfResult get(String url, String label){
		if(label == null || label.equalsIgnoreCase("")){
			return null;
		}

		BufferedReader rd = null;
		HttpGet request = new HttpGet(url+label);
		try{
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode() != 200){
				return null;
			}
			
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			synchronized (unmarshaller) {
				return (ArrayOfResult) unmarshaller.unmarshal(new StringReader(result.toString()));	
			}
			
		}catch(Throwable e){
			e.printStackTrace();
		}finally{
			cleanup(rd, request);
		}
		return null;
	}
	
	private void cleanup(BufferedReader rd, HttpGet request){
		
		try{
			if(rd != null){
				rd.close();
			}
			request.releaseConnection();			
		}catch(Throwable e){
			rd = null;
			request = null;
		}
	}
}
