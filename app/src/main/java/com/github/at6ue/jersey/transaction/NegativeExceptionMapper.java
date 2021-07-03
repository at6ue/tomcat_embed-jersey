package com.github.at6ue.jersey.transaction;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NegativeExceptionMapper implements ExceptionMapper<NegativeException> {

    @Override
    public Response toResponse(NegativeException exception) {
        return Response.status(Status.BAD_REQUEST).build();
    }
    
}
