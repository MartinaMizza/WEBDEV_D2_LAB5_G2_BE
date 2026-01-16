package it.packovery.web.model.exception;

import it.packovery.service.exception.InvalidCredentialsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidCredentialsExceptionMapper implements ExceptionMapper<InvalidCredentialsException> {

    @Override
    public Response toResponse(InvalidCredentialsException ex) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("INVALID_CREDENTIALS", ex.getMessage()))
                .build();
    }
}
