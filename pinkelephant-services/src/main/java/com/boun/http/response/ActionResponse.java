package com.boun.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActionResponse {

    @JsonProperty("ack")
    private boolean acknowledge;

    public ActionResponse(){}

    public ActionResponse(boolean acknowledge){
        this.acknowledge = acknowledge;
    }
}
