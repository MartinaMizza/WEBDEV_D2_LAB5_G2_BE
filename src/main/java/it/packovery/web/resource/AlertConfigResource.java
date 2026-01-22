package it.packovery.web.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.packovery.service.AlertConfigService;
import it.packovery.service.SecurityService;
import it.packovery.web.model.AlertConfigResponse;
import it.packovery.web.model.CreateAlertConfigRequest;
import it.packovery.web.model.UpdateAlertConfigRequest;
import it.packovery.web.model.UpdateStateAlertConfigRequest;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
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
    public Response getAllAlertConfigs(@Context SecurityContext securityContext) {
        String userEmail = securityContext.getUserPrincipal().getName();
        List<AlertConfigResponse> alertConfigResponseList = alertConfigService.getAllAlertsConfigByUserEmail(userEmail);

        return Response.ok(alertConfigResponseList).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response createAlertConfig(
            @Context SecurityContext securityContext,
            CreateAlertConfigRequest alertConfigRequest
    ) {
        String userEmail = securityContext.getUserPrincipal().getName();

        LOG.infof("SECURITY EVENT - User [%s] is creating a new Alert Config: [Name: %s, Type: %s]",
                userEmail, alertConfigRequest.getName(), alertConfigRequest.getType());

        AlertConfigResponse alertConfigResponse = alertConfigService.createAlertConfig(userEmail, alertConfigRequest);

        return Response.ok(alertConfigResponse).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @RolesAllowed({"access_token"})
    public Response updateAlertConfig(
            @Context SecurityContext securityContext,
            @PathParam("id") String id,
            UpdateAlertConfigRequest updateAlertConfigRequest
    ) {
        String userEmail = securityContext.getUserPrincipal().getName();
        String sanitizedId = securityService.sanitize(id);

        LOG.infof("SECURITY EVENT - User [%s] updated Alert Config ID: [%s]. New Threshold: [%s], State: [%b]",
                userEmail, sanitizedId, updateAlertConfigRequest.getThreshold(), updateAlertConfigRequest.getState());

        AlertConfigResponse alertConfigResponse = alertConfigService.updateAlertConfig(
                sanitizedId,
                updateAlertConfigRequest,
                userEmail
        );

        return Response.ok(alertConfigResponse).build();
    }

    @PUT
    @Path("/state/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @RolesAllowed({"access_token"})
    public Response updateAlertConfigState(
            @Context SecurityContext securityContext,
            @PathParam("id") String id,
            UpdateStateAlertConfigRequest updateStateAlertConfigRequest
    ) {
        String userEmail = securityContext.getUserPrincipal().getName();
        String sanitizedId = securityService.sanitize(id);

        LOG.infof("SECURITY EVENT - User [%s] updated Alert Config ID: [%s]. New Threshold: [%s], State: [%b]",
                userEmail, sanitizedId, updateStateAlertConfigRequest.getState());

        AlertConfigResponse alertConfigResponse = alertConfigService.updateStateAlertConfig(
                sanitizedId,
                updateStateAlertConfigRequest,
                userEmail
        );

        return Response.ok(alertConfigResponse).build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response deleteAlertConfig(
            @Context SecurityContext securityContext,
            @PathParam("id") String id
    ) {
        String userEmail = securityContext.getUserPrincipal().getName();
        String sanitizedId = securityService.sanitize(id);

        LOG.warnf("SECURITY EVENT - User [%s] is deleting Alert Config ID: [%s]",
                userEmail, sanitizedId);

        AlertConfigResponse alertConfigResponse = alertConfigService.deleteAlertConfig(sanitizedId, userEmail);

        return Response.ok(alertConfigResponse).build();
    }
}
