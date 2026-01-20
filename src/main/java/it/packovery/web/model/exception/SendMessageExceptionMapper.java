package it.packovery.web.model.exception;

import it.packovery.service.exception.SendMessageException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SendMessageExceptionMapper implements ExceptionMapper<SendMessageException> {

    @Override
    public Response toResponse(SendMessageException ex) {
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .entity(new ErrorResponse("CANNOT_SEND_MESSAGE", ex.getMessage()))
                .build();
    }
}
