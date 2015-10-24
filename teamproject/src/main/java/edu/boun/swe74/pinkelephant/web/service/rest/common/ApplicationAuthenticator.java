package edu.boun.swe74.pinkelephant.web.service.rest.common;

import java.util.UUID;

import javax.ejb.Local;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Local
public final class ApplicationAuthenticator {

	private static final Logger logger = LogManager.getLogger(ApplicationAuthenticator.class);
	
	public ApplicationAuthenticator() {
	}

	public AuthenticationResponse authenticate(String username, String password){
		
		String authToken = UUID.randomUUID().toString();
		
		PESession.getInstance().putToken(authToken, username);
		
		AuthenticationResponse response = new AuthenticationResponse(true, authToken, null);
		
		return response;
	}
	
	public boolean checkIfAuthorized(String token) {

		String user = PESession.getInstance().getUser(token);
		if (user == null) {
			logger.error("User is not authorized, token->" + token);
			return false;
		}

		if(logger.isDebugEnabled()){
			logger.debug("User is authorized, token->" + token + ", username->" + user);
		}
		
		return true;
	}
	
	public static class AuthenticationResponse{
		
		private boolean result;
		private String token;
		private String errorMessage;
		
		public AuthenticationResponse(boolean result, String token, String errorMessage) {
			this.result = result;
			this.token = token;
			this.errorMessage = errorMessage;
		}
		
		public boolean isResult() {
			return result;
		}
		
		public void setResult(boolean result) {
			this.result = result;
		}
		
		public String getToken() {
			return token;
		}
		
		public void setToken(String token) {
			this.token = token;
		}
		
		public String getErrorMessage() {
			return errorMessage;
		}
		
		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}
	}
}
