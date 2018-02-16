package org.pollanz.samples.api.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    private static final Logger log = LoggerFactory.getLogger(UnauthorizedEntryPoint.class);

    public UnauthorizedEntryPoint() {
    }

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg2) throws IOException, ServletException {
        log.debug("Pre-authenticated entry point called. Rejecting access");
        response.sendError(401, "Access Denied");
    }
}
