package edu.boun.swe74.pinkelephant.web.service.rest;

import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import edu.boun.swe74.pinkelephant.web.service.rest.common.ApplicationAuthenticator;
import edu.boun.swe74.pinkelephant.web.service.rest.common.ApplicationAuthenticator.AuthenticationResponse;

@Path("/pinkelephant")
public class RestInterface {
	
	@EJB
	ApplicationAuthenticator authenticator;
	
	@POST
	@Path("/login/{username}")
	public String login(@Context HttpHeaders httpHeaders, @PathParam("username") String username, @FormParam("password") String password) {
 

		AuthenticationResponse response = authenticator.authenticate(username, password);
		
		if(response.isResult()){
			return response.getToken();
		}else{
			return response.getErrorMessage().getValue();
		}
	}

}
