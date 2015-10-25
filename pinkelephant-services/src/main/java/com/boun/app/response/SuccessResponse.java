package com.boun.app.response;

public class SuccessResponse extends Response {

    private Object result;

    public SuccessResponse() {
        super("success");
    }

    public SuccessResponse(Object result) {
        super("success");
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
