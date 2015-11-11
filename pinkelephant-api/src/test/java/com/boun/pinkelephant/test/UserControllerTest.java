package com.boun.pinkelephant.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.boun.PinkElephantApiApplication;
import com.boun.data.common.enums.Status;
import com.boun.data.mongo.model.User;
import com.boun.data.mongo.model.UserDetail;
import com.boun.data.mongo.repository.UserRepository;
import com.boun.http.request.AuthenticationRequest;
import com.boun.http.request.CreateUserRequest;
import com.boun.http.request.ResetPasswordRequest;
import com.boun.http.response.ActionResponse;
import com.boun.http.response.LoginResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration(classes = PinkElephantApiApplication.class)
@WebIntegrationTest("server.port:0")
public class UserControllerTest {

	@Value("${local.server.port}")
	private int port;

	@Autowired private UserRepository userRepository;
	
	private static CreateUserRequest createUserRequest = getDummyUser();
	
	@Test
	public void test1_createUser(){
		
		ResponseEntity<ActionResponse> entity = new TestRestTemplate().postForEntity(getServiceUrl("create"), createUserRequest, ActionResponse.class);
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(true, entity.getBody().isAcknowledge());
	}
	
	@Test
	public void test2_login(){
		
		AuthenticationRequest request = new AuthenticationRequest();
		request.setUsername(createUserRequest.getUser().getUsername());
		request.setPassword(createUserRequest.getUser().getPassword());
		
		ResponseEntity<LoginResponse> entity = new TestRestTemplate().postForEntity(getServiceUrl("login"), request, LoginResponse.class);
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		//assertEquals(true, entity.getBody().isAcknowledge());
		assertNotNull(entity.getBody().getToken());
	}
	
	@Test
	public void test3_resetPassword(){
		
		ResetPasswordRequest request = new ResetPasswordRequest();
		request.setUsername(createUserRequest.getUser().getUsername());
		
		ResponseEntity<ActionResponse> entity = new TestRestTemplate().postForEntity(getServiceUrl("resetPassword"), request, ActionResponse.class);
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(true, entity.getBody().isAcknowledge());
	}
	
	@Test
	public void test4_deleteUser(){
		
//		boolean result = userRepository.deleteUser(createUserRequest.getUser().getUsername());
//		
//		assertEquals(true, result);
	}
	
	private String getServiceUrl(String method){
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://localhost:");
		buffer.append(port);
		buffer.append("/v1/user/").append(method);
		
		return buffer.toString();
	}
	
	private static CreateUserRequest getDummyUser(){
		
		User user = new User();
		user.setFirstname("TestFirstUsername");
		user.setLastname("TestLastName");
		user.setPassword("TestPassword");
		user.setStatus(Status.ACTIVE);
		user.setUsername(new UsernameGenerator().nextUsername() + "@gmail.com");
		
		UserDetail detail = new UserDetail();
		detail.setBirthDate(new Date());
		
		user.setUserDetail(detail);
		CreateUserRequest request = new CreateUserRequest();
		request.setUser(user);
		
		return request;
	}
	
	private static final class UsernameGenerator {
		  private SecureRandom random = new SecureRandom();

		  public String nextUsername() {
		    return new BigInteger(130, random).toString(32);
		  }
		}
}
