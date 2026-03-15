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
import org.jboss.logging.MDC;

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
        MDC.put("user_email", userEmail);
        MDC.put("event_type", "create_alert_config");

        AlertConfigResponse alertConfigResponse = alertConfigService.createAlertConfig(userEmail, alertConfigRequest);
        MDC.put("event_outcome", "success");
        LOG.info("SECURITY EVENT - Config created: " + alertConfigResponse.getName());

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
        MDC.put("user_email", userEmail);
        MDC.put("event_type", "update_alert_config");

        String sanitizedId = securityService.sanitize(id);

        AlertConfigResponse alertConfigResponse = alertConfigService.updateAlertConfig(
                sanitizedId,
                updateAlertConfigRequest,
                userEmail
        );

        MDC.put("event_outcome", "success");
        LOG.info("SECURITY EVENT - Config updated: " + alertConfigResponse.getName());

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

        MDC.put("user_email", userEmail);
        MDC.put("event_type", "update_alert_config_state");

        String sanitizedId = securityService.sanitize(id);

        AlertConfigResponse alertConfigResponse = alertConfigService.updateStateAlertConfig(
                sanitizedId,
                updateStateAlertConfigRequest,
                userEmail
        );

        MDC.put("event_outcome", "success");
        LOG.info("SECURITY EVENT - Config state updated: " + alertConfigResponse.getName());

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

        MDC.put("user_email", userEmail);
        MDC.put("event_type", "delete_alert_config");

        String sanitizedId = securityService.sanitize(id);

        AlertConfigResponse alertConfigResponse = alertConfigService.deleteAlertConfig(sanitizedId, userEmail);

        MDC.put("event_outcome", "success");
        LOG.info("SECURITY EVENT - Config deleted: " + alertConfigResponse.getName());

        return Response.ok(alertConfigResponse).build();
    }
}
