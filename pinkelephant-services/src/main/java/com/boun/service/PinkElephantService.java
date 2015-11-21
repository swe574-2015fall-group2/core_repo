package com.boun.service;

import com.boun.app.common.ErrorCode;
import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.exception.PinkElephantValidationException;
import com.boun.data.session.PinkElephantSession;
import com.boun.http.request.BaseRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public abstract class PinkElephantService {

    @Autowired
    protected Validator validator;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void validate(Object request) throws PinkElephantValidationException {
        Set<? extends ConstraintViolation<?>> constraintViolations = validator.validate(request);
        if (constraintViolations.size() > 0) {
            throw new PinkElephantValidationException(constraintViolations);
        }
    }
    
    protected void validate(BaseRequest request) throws PinkElephantRuntimeException{
    	
    	if(request.getAuthToken() == null || "".equalsIgnoreCase(request.getAuthToken())){
    		throw new PinkElephantRuntimeException(400, ErrorCode.INVALID_INPUT, "Authentication token field is empty", "");
    	}
    	if (!PinkElephantSession.getInstance().validateToken(request.getAuthToken())) {
    		throw new PinkElephantRuntimeException(400, ErrorCode.OPERATION_NOT_ALLOWED, "");
		}
    }

    protected void validate(String authToken) throws PinkElephantRuntimeException{

        BaseRequest request = new BaseRequest();
        request.setAuthToken(authToken);

        validate(request);
    }
}
