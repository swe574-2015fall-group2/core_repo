package com.boun.app.client;

import com.boun.app.exception.PinkElephantRuntimeException;
import com.boun.data.mongo.model.User;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


@Component
public class PinkElephantHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(PinkElephantHttpClient.class);

    private CloseableHttpClient client;
    private ObjectMapper objectMapper;

    @Value("${api.pinkelephantServiceTimeout:4000}") private int timeout;

    @PostConstruct
    private void initializeClient() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout)
                .build();
        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        objectMapper = new ObjectMapper( new JsonFactory() );
        logger.info("Successfully created PinkElephantHttpClient");
    }

    private <T> T getResponseContent(HttpResponse response, Class<T> clazz) throws IOException {
        HttpEntity entity = response.getEntity();
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }
        InputStream is = entity.getContent();
        return objectMapper.readValue(is, clazz);
    }
}
