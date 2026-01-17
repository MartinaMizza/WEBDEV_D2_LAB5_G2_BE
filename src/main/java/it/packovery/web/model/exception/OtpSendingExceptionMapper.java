package it.packovery.web.model.exception;

import it.packovery.service.exception.OtpSendingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class OtpSendingExceptionMapper implements ExceptionMapper<OtpSendingException> {

    @Override
    public Response toResponse(OtpSendingException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("ERROR_WHILE_SENDING_OTP", ex.getMessage()))
                .build();
    }
}
