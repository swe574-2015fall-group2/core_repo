package com.boun.web.advice;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.app.exception.PinkElephantValidationException;
import com.boun.app.response.ErrorResponse;
import com.boun.app.response.Response;
import com.boun.app.response.SuccessResponse;
import com.boun.app.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class PinkElephantControllerAdvice extends ResponseEntityExceptionHandler implements ResponseBodyAdvice<Object> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @ExceptionHandler(PinkElephantRuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse runtimeExceptionHandler(PinkElephantRuntimeException ex) {
        ErrorResponse er = ex.getErrorResponse();
        logger.error("Runtime Exception: [ErrorCode:"+er.getErrorCode()+"][Message:"+er.getApplicationMessage()+"]");
        return er;
    }

    @ResponseBody
    @ExceptionHandler(PinkElephantValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptionHandler(PinkElephantValidationException ex) {
        ErrorResponse er = ex.getErrorResponse();
        logger.error("Validation Error: [ErrorCode:"+er.getErrorCode()+"][Message:"+er.getApplicationMessage()+"]");
        return ex.getErrorResponse();
    }

    @ResponseBody
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse otherExceptionHandler(Throwable ex) {
        while (ex.getCause() != null) ex = ex.getCause();
        logger.error("Unhandled Exception: [Cause:"+ex.getMessage()+"]", ex);
        return new ErrorResponse().setApplicationMessage("Internal Server Error");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse().setApplicationMessage("Invalid request")
                .setConsumerMessage("HttpMessageNotReadable, Please check your request");
        return new ResponseEntity<Object>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse().setApplicationMessage("Invalid request")
                .setConsumerMessage(ex.getMessage());
        return new ResponseEntity<Object>(response, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse().setApplicationMessage("Invalid request")
                .setConsumerMessage(ex.getMessage());
        return new ResponseEntity<Object>(response, headers, status);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        if (body instanceof Response) return body;
        if (ObjectUtils.isNotNull(body) && body.getClass().getPackage().getName().startsWith("com.mangofactory.swagger.models") ) {
            return body; // Don't wrap swagger responses
        }

        // Handle Filter exception and convert it to Error Message
        if(ObjectUtils.isNotNull(body) && (body instanceof LinkedHashMap)){
            Map exceptionMap = (LinkedHashMap)body;
            String errorMessage =  exceptionMap.get("message").toString();
            ErrorResponse errorResponse =  new ErrorResponse().setApplicationMessage("Invalid Request");
            if(ObjectUtils.isNotNull(errorMessage)) {
                if(errorMessage.equals("Invalid Token")){
                    errorResponse.setErrorCode("200");
                    errorResponse.setApplicationMessage("Invalid Token");
                    errorResponse.setConsumerMessage("You should specify a valid token");
                }
                if(errorMessage.equals("Unauthorized Operation")){
                    errorResponse.setErrorCode("210");
                    errorResponse.setApplicationMessage("Unauthorized Operation");
                    errorResponse.setConsumerMessage("You don't have permission to take this action");
                }
            }

            return errorResponse;
        }

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResult(body);
        return successResponse;
    }
}
