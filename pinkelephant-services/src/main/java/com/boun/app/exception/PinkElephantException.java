package com.boun.app.exception;

import com.boun.app.response.ErrorResponse;
import com.boun.app.bundle.PinkElephantErrorBundle;

public class PinkElephantException extends RuntimeException {

    private String key;

    public PinkElephantException(String key){
        this.key = key;
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(PinkElephantErrorBundle.getErrorCode(key));
        response.setApplicationMessage(PinkElephantErrorBundle.getApplicationErrorMessage(key));
        response.setConsumerMessage(PinkElephantErrorBundle.getConsumerErrorMessage(key));
        return response;
    }
}
