package it.packovery.web.resource;

import it.packovery.data.model.Alert;
import it.packovery.service.AlertService;
import it.packovery.web.model.exception.AlertResponse;
import jakarta.annotation.security.DenyAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/alert")
@DenyAll
public class AlertResource {

    private final AlertService alertService;
    public AlertResource(final AlertService alertService) {
        this.alertService = alertService;
    }

    @GET
    public List<AlertResponse> getAlerts() {
        return alertService.getAlerts();
    }

    @GET
    @Path("/pending")
    public List<AlertResponse> getPendingAlerts() {
        return alertService.getPendingAlerts();
    }

    @GET
    @Path("/{id}")
    public AlertResponse getAlert(@PathParam("id") Long id) {
        return alertService.getAlertById(id);
    }

    @PUT
    @Path("/resolve/{id}")
    public Response resolveAlert(@PathParam("id") Long id, Alert alert) {
        if(alertService.resolveAlert(id, alert)){
            return Response.ok("Alert resolved.").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/automatic/resolve")
    public Response automaticResolveAlert() {
        if(alertService.automaticResolveAlert()){
            return Response.ok("Alert automatically resolved.").build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
