package it.packovery.web.model.exception;

import it.packovery.service.exception.AccountTemporarilyBlockedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AccountTemporarilyExceptionMapper implements ExceptionMapper<AccountTemporarilyBlockedException> {

    @Override
    public Response toResponse(AccountTemporarilyBlockedException ex) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("ACCOUNT_TEMPORARILY_BLOCKED", ex.getMessage()))
                .build();
    }
}
