package com.boun.app.exception;

import com.boun.app.response.ErrorResponse;

public class PinkElephantRuntimeException extends RuntimeException {

    private int status;
    private String errorCode;
    private String errorMessage;
    private String developerMessage;

    public PinkElephantRuntimeException(int httpStatus, String errorCode, String errorMessage, String developerMessage) {
        this.status = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.developerMessage = developerMessage;
    }

//    public Response getResponse() {
//        return new Response(status, getErrorResponse());
//    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    public ErrorResponse getErrorResponse() {
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(errorCode);
        response.setApplicationMessage(developerMessage);
        response.setConsumerMessage(errorMessage);
        return response;
    }
}
