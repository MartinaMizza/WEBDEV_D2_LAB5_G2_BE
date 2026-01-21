package it.packovery.web.model.exception;

import it.packovery.service.exception.AccountPermanentlyBlockedException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AccountPermanentlyBlockedExceptionMapper implements ExceptionMapper<AccountPermanentlyBlockedException> {

    @Override
    public Response toResponse(AccountPermanentlyBlockedException ex) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse("ACCOUNT_PERMAMENTLY_BLOCKED", ex.getMessage()))
                .build();
    }
}
