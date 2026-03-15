package it.packovery.web.resource;

import it.packovery.service.AlertService;
import it.packovery.web.model.exception.AlertResponse;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import java.util.List;

@Path("/api/alert")
@DenyAll
public class AlertResource {

    private static final Logger LOG = Logger.getLogger(AlertResource.class);
    private final AlertService alertService;

    public AlertResource(final AlertService alertService) {
        this.alertService = alertService;
    }


    @GET
    @RolesAllowed({"access_token"})
    public List<AlertResponse> getAlerts() {
        return alertService.getAlerts();
    }

    @GET
    @RolesAllowed({"access_token"})
    @Path("/pending")
    public List<AlertResponse> getPendingAlertsByUser(@Context SecurityContext securityContext) {
        String userEmail = securityContext.getUserPrincipal().getName();

        return alertService.getPendingAlertsByUser(userEmail);
    }

    @GET
    @RolesAllowed({"access_token"})
    @Path("/{id}")
    public AlertResponse getAlert(@PathParam("id") Long id) {
        return alertService.getAlertById(id);
    }

    @PUT
    @RolesAllowed({"access_token"})
    @Path("/resolve/{id}")
    public Response resolveAlert(
            @Context SecurityContext securityContext,
            @PathParam("id") Long id
    ) {
        String userEmail = securityContext.getUserPrincipal().getName();

        MDC.put("user_email", userEmail);
        MDC.put("event_type", "alert_resolution");

        alertService.resolveAlert(id, userEmail);

        MDC.put("event_outcome", "success");
        LOG.info("SECURITY EVENT - Resolved alert with id: " + id);

        return Response.ok("Alert resolved.").build();
    }

    @PUT
    @RolesAllowed({"access_token"})
    @Path("/automatic/resolve")
    public Response automaticResolveAlert() {
        MDC.put("user_email", "SYSTEM");
        MDC.put("event_type", "alert_automatic_resolution");

        MDC.put("event_outcome", "success");
        LOG.infof("SECURITY EVENT - Automatic alert resolution started");

        /*
        if(alertService.automaticResolveAlert()){
            LOG.infof("SECURITY EVENT - SYSTEM ACTION - Automatic alert resolution completed successfully");
            return Response.ok("Alert automatically resolved.").build();
        }
         */

        LOG.errorf("SECURITY EVENT - SYSTEM ACTION - Automatic alert resolution failed");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
