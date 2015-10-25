package com.boun.http.response;

import lombok.Data;

@Data
public class PhotoResponse {

    private String url;

    private String name = "";

    private String teamName = "";

    public PhotoResponse(String url) {
        this.url = url;
    }
}
