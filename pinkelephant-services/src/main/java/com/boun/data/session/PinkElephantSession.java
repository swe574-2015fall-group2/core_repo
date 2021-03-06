package com.boun.data.session;

import java.util.Hashtable;

import com.boun.data.mongo.model.User;

public final class PinkElephantSession {

	private static Hashtable<String, User> sessionStorage = new Hashtable<String, User>();
	
	private static PinkElephantSession instance = new PinkElephantSession();
	
	private PinkElephantSession(){
	}
	
	public static PinkElephantSession getInstance() {
		return instance;
	}
	
	public boolean validateToken(String token){
		if(token == null){
			return false;
		}
		return sessionStorage.get(token) != null;
	}
	
	public void addToken(String token, User user){
		sessionStorage.put(token, user);
	}
	
	public void removeToken(String token){
		sessionStorage.remove(token);
	}
	
	public User getUser(String token){
		return sessionStorage.get(token);
	}
}
