package edu.boun.swe74.pinkelephant.web.service.rest.common;

import java.util.Hashtable;

public class PESession {

	private static PESession instance = new PESession();
	
	private Hashtable<String, String> tokenTable = new Hashtable<>();
	
	public static PESession getInstance() {
		return instance;
	}
	
	private PESession() {
	}
	
	public void putToken(String token, String user){
		tokenTable.put(token, user);
	}
	
	public String getUser(String token){
		return tokenTable.get(token);
	}
	
}
