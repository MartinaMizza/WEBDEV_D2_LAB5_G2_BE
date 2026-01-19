package it.packovery.web.resource;

import it.packovery.service.AlertConfigService;
import it.packovery.web.model.AlertConfigResponse;
import it.packovery.web.model.CreateAlertConfigRequest;
import it.packovery.web.model.UpdateAlertConfigRequest;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/alert-config")
@DenyAll
public class AlertConfigResource {

    private final AlertConfigService alertConfigService;

    public AlertConfigResource(AlertConfigService alertConfigService) {
        this.alertConfigService = alertConfigService;
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
        AlertConfigResponse alertConfigResponse = alertConfigService.updateAlertConfig(id, updateAlertConfigRequest);

        return Response.ok(alertConfigResponse).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response deleteAlertConfig(@PathParam("id") String id) {
        AlertConfigResponse alertConfigResponse = alertConfigService.deleteAlertConfig(id);

        return Response.ok(alertConfigResponse).build();
    }

}
