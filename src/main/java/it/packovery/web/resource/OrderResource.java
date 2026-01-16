package it.packovery.web.resource;

import it.packovery.data.model.Order;
import it.packovery.service.OrderService;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/api/backoffice/orders")
@DenyAll
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GET
    @RolesAllowed("access-token")
    @Path("/{id}")
    public Order getDetail(@PathParam("id") Long id) {
        Long userId = 0L;
        return orderService.getOrderDetail(id, userId);
    }
}
