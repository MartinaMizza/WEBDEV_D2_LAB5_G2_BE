package it.packovery.web.model.exception;

import it.packovery.service.exception.AlertConfigAlreadyExistsException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AlertConfigAlreadyExistsExceptionMapper implements ExceptionMapper<AlertConfigAlreadyExistsException> {

    @Override
    public Response toResponse(AlertConfigAlreadyExistsException ex) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("ALERT_CONFIG_ALREADY_EXISTS", ex.getMessage()))
                .build();
    }
}
