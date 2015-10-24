package edu.boun.swe74.pinkelephant.web.service.rest.common;

import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.boun.swe74.pinkelephant.db.dao.UserDao;
import edu.boun.swe74.pinkelephant.db.model.User;

@Local
@Stateless
public final class ApplicationAuthenticator {

	private static final Logger logger = LogManager.getLogger(ApplicationAuthenticator.class);
	
	@EJB
	UserDao userDao;
	
	public ApplicationAuthenticator() {
	}

	public AuthenticationResponse authenticate(String username, String password){
		
		try{
			User user = userDao.getUser(username, password);
			if(user == null){
				logger.error("Authentication is failed for user, username->" + username + ", password->" + password);
				return new AuthenticationResponse(false, null, ErrorMessage.AUTHENTICATION_FAILED);
			}
			
			String authToken = UUID.randomUUID().toString();
			
			PESession.getInstance().putToken(authToken, user);
			
			return new AuthenticationResponse(true, authToken, null);
			
		}catch(Throwable e){
			logger.error("Error occured in authenticate()", e);
			
			return new AuthenticationResponse(false, null, ErrorMessage.UNEXPECTED_ERROR);
		}
		
	}
	
	public boolean checkIfAuthorized(String token) {

		User user = PESession.getInstance().getUser(token);
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
		private ErrorMessage errorMessage;
		
		public AuthenticationResponse(boolean result, String token, ErrorMessage errorMessage) {
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
		
		public ErrorMessage getErrorMessage() {
			return errorMessage;
		}
		
		public void setErrorMessage(ErrorMessage errorMessage) {
			this.errorMessage = errorMessage;
		}
	}
}
