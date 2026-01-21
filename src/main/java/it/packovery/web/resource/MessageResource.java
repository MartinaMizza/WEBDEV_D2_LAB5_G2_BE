package it.packovery.web.resource;

import it.packovery.service.MessageService;
import it.packovery.web.model.SendMessageRequest;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.jboss.logging.Logger;

@Path("/api/message")
public class MessageResource {

    private static final Logger LOG = Logger.getLogger(MessageResource.class);
    private final MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response createMessage(@Context SecurityContext securityContext,
                                  SendMessageRequest sendMessageRequest
    ) {
        String email = securityContext.getUserPrincipal().getName();

        LOG.infof("SECURITY EVENT - User [%s] is sending a message related to Order ID: [%s]",
                email, sendMessageRequest.getOrderId());
        messageService.createMessage(sendMessageRequest, email);

        return Response.ok().build();
    }
}
