package it.packovery.web.model.exception;

import it.packovery.service.exception.UnmatchingPasswordsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnmatchingPasswordsExceptionMapper implements ExceptionMapper<UnmatchingPasswordsException> {

    @Override
    public Response toResponse(UnmatchingPasswordsException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("PASSWORDS_NOT_MATCHING", ex.getMessage()))
                .build();
    }
}
