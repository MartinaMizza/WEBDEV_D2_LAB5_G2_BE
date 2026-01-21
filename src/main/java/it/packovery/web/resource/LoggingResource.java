package it.packovery.web.resource;

import it.packovery.service.LoggingService;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/logging")
@DenyAll
public class LoggingResource {

    private final LoggingService loggingService;

    public LoggingResource(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @PUT
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response updatedAllLogsByUserId(@Context SecurityContext securityContext) {
        String email = securityContext.getUserPrincipal().getName();
        loggingService.updateLogRecordsAtLogout(email);

        return Response.ok().build();
    }
}
