package it.packovery.web.model.exception;

import it.packovery.service.exception.InvalidOtpException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidOtpExceptionMapper implements ExceptionMapper<InvalidOtpException> {

    @Override
    public Response toResponse(InvalidOtpException ex) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("INVALID_OTP", ex.getMessage()))
                .build();
    }
}
