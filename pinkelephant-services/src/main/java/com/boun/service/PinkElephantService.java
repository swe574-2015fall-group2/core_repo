package com.boun.service;

import com.boun.app.exception.PinkElephantValidationException;
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
}
