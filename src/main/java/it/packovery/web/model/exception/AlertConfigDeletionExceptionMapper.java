package it.packovery.web.model.exception;

import it.packovery.service.exception.AlertConfigDeletionException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AlertConfigDeletionExceptionMapper implements ExceptionMapper<AlertConfigDeletionException> {

    @Override
    public Response toResponse(AlertConfigDeletionException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ERROR_DELETING_ALERT_CONFIGURATION", ex.getMessage()))
                .build();
    }
}
