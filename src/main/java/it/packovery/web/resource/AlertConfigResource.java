package it.packovery.web.resource;

import it.packovery.service.AlertConfigService;
import it.packovery.service.SecurityService;
import it.packovery.web.model.AlertConfigResponse;
import it.packovery.web.model.CreateAlertConfigRequest;
import it.packovery.web.model.UpdateAlertConfigRequest;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.util.List;

@Path("/api/alert-config")
@DenyAll
public class AlertConfigResource {

    private static final Logger LOG = Logger.getLogger(AlertConfigResource.class);
    private final AlertConfigService alertConfigService;
    private final SecurityService securityService;

    public AlertConfigResource(AlertConfigService alertConfigService, SecurityService securityService) {
        this.alertConfigService = alertConfigService;
        this.securityService = securityService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response getAllAlertConfigs() {
        List<AlertConfigResponse> alertConfigResponseList = alertConfigService.getAllAlertsConfig();

        return Response.ok(alertConfigResponseList).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response createAlertConfig(CreateAlertConfigRequest alertConfigRequest) {
        LOG.infof("SECURITY EVENT - User is creating a new Alert Config: [Name: %s, Type: %s]",
                alertConfigRequest.getName(), alertConfigRequest.getType());

        AlertConfigResponse alertConfigResponse = alertConfigService.createAlertConfig(alertConfigRequest);

        return Response.ok(alertConfigResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response updateAlertConfig(
            @PathParam("id") String id,
            UpdateAlertConfigRequest updateAlertConfigRequest
    ) {
        String sanitizedId = securityService.sanitize(id);

        LOG.infof("SECURITY EVENT - User updated Alert Config ID: [%s]. New Threshold: [%s], State: [%b]",
                id, updateAlertConfigRequest.getThreshold(), updateAlertConfigRequest.getState());

        AlertConfigResponse alertConfigResponse = alertConfigService.updateAlertConfig(sanitizedId, updateAlertConfigRequest);

        return Response.ok(alertConfigResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response deleteAlertConfig(@PathParam("id") String id) {
        String sanitizedId = securityService.sanitize(id);

        LOG.warnf("SECURITY EVENT - User is attempting to DELETE Alert Config ID: [%s]", sanitizedId);

        AlertConfigResponse alertConfigResponse = alertConfigService.deleteAlertConfig(sanitizedId);

        return Response.ok(alertConfigResponse).build();
    }
}
