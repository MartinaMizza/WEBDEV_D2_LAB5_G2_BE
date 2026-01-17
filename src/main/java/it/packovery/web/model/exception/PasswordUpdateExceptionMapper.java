package it.packovery.web.model.exception;

import it.packovery.service.exception.PasswordUpdateException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PasswordUpdateExceptionMapper implements ExceptionMapper<PasswordUpdateException> {

    @Override
    public Response toResponse(PasswordUpdateException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ERROR_UPDATING_PASSWORD", ex.getMessage()))
                .build();
    }
}
