package it.packovery.web.model.exception;

import it.packovery.service.exception.PasswordResetTokenCreationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PasswordResetTokenCreationExceptionMapper implements ExceptionMapper<PasswordResetTokenCreationException> {

    @Override
    public Response toResponse(PasswordResetTokenCreationException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ERROR_SAVING_PASSWORD_RESET_TOKEN", ex.getMessage()))
                .build();
    }
}
