package org.pollanz.samples.api.gateway.utils;

import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class ResponseUtils {

    private final static Collection<String> HEADERS_TO_FORWARD;

    static {
        HEADERS_TO_FORWARD = new HashSet<>(2);
        HEADERS_TO_FORWARD.add(HttpHeaders.CONTENT_DISPOSITION);
        HEADERS_TO_FORWARD.add(HttpHeaders.CONTENT_TYPE);
    }

    private static boolean toBeForwarded(final String headerName) {
        if (StringUtils.isBlank(headerName)) {
            return false;
        }
        return HEADERS_TO_FORWARD.stream().filter(s -> StringUtils.equalsIgnoreCase(s, headerName)).count() > 0;
    }

    /**
     * Build new response from {@code response} in input, by performing a shallow copy.
     *
     * @param response response to copy
     * @return new response
     * @see Response
     */
    public static Response forward(final Response response) {
        Response.ResponseBuilder builder = Response.status(response.getStatus());
        if (response.hasEntity()) {
            builder.entity(response.getEntity());
        }

        for (String headerName : response.getHeaders().keySet()) {
            if (toBeForwarded(headerName)) {
                List<Object> headerValues = response.getHeaders().get(headerName);
                for (Object headerValue : headerValues) {
                    builder.header(headerName, headerValue);
                }
            }
        }

        return builder.build();
    }

}
