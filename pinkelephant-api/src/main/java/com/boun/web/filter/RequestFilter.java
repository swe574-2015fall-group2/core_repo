package com.boun.web.filter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RequestFilter implements Filter {

    @Value("${app.auth.service}") private String AUTHENTICATION_SERVICE;

    @Value("${app.clean.service}") private String CLEAN_DATA_ENDPOINT;

    @Value("${app.system.health.check}") private String SYSTEM_HEALTH_CHECK;

    @Value("${app.admin.service}") private String ADMIN_SERVICE;

    @Value("${app.swagger.docs}") private String SWAGGER;

    @Value("${app.favicon.ico}") private String FAVICON;

    @Value("${app.header.x.forwarded}") private String HEADER_X_FORWARDED;

    @Value("${app.header.token}") private String HEADER_TOKEN;

    @Value("${app.header.channel}") private String HEADER_CHANNEL;

    @Value("${app.header.ip}") private String HEADER_IP;

    @Value("${app.http.options}") private String OPTIONS;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}
