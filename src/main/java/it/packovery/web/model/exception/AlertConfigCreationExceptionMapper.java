package it.packovery.web.model.exception;

import it.packovery.service.exception.AlertConfigCreationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AlertConfigCreationExceptionMapper implements ExceptionMapper<AlertConfigCreationException> {

    @Override
    public Response toResponse(AlertConfigCreationException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ERROR_SAVING_ALERT_CONFIGURATION", ex.getMessage()))
                .build();
    }
}
