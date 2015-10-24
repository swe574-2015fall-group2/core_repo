package edu.boun.swe74.pinkelephant.web.service.rest.common;

import java.util.Hashtable;

import edu.boun.swe74.pinkelephant.db.model.User;

public class PESession {

	private static PESession instance = new PESession();
	
	private Hashtable<String, User> tokenTable = new Hashtable<String, User>();
	
	public static PESession getInstance() {
		return instance;
	}
	
	private PESession() {
	}
	
	public void putToken(String token, User user){
		tokenTable.put(token, user);
	}
	
	public User getUser(String token){
		return tokenTable.get(token);
	}
	
}
