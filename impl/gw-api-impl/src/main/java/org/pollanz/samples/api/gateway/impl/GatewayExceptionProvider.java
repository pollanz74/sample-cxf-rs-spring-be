package org.pollanz.samples.api.gateway.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class GatewayExceptionProvider implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        log.error("Uncaught error", exception);

        if (exception instanceof WebApplicationException) {
            Response response = ((WebApplicationException) exception).getResponse();
            if (response == null) {
                return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()).type(MediaType.TEXT_PLAIN).build();
            }
            return Response.status(response.getStatus()).entity(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()).type(MediaType.TEXT_PLAIN).build();
        } else if (exception instanceof AccessDeniedException) {
            return Response.status(Response.Status.FORBIDDEN).entity(Response.Status.FORBIDDEN.getReasonPhrase()).type(MediaType.TEXT_PLAIN).build();
        }

        return Response.serverError().entity(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase()).type(MediaType.TEXT_PLAIN).build();

    }

}
