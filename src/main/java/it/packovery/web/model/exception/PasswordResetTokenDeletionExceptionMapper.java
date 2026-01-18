package it.packovery.web.model.exception;

import it.packovery.service.exception.PasswordResetTokenDeletionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PasswordResetTokenDeletionExceptionMapper implements ExceptionMapper<PasswordResetTokenDeletionException> {

    @Override
    public Response toResponse(PasswordResetTokenDeletionException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ERROR_DURING_PASSWORD_RESET_TOKEN_DELETION", ex.getMessage()))
                .build();
    }
}
