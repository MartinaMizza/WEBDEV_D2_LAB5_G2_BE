package it.packovery.web.resource;

import it.packovery.data.model.enumModel.OrderStatus;
import it.packovery.data.model.enumModel.PackageSize;
import it.packovery.data.model.enumModel.PackageWeight;
import it.packovery.service.OrderService;
import it.packovery.service.SecurityService;
import it.packovery.web.model.OrderDetailsResponse;
import it.packovery.web.model.OrderResponse;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/api/orders")
@DenyAll
public class OrderResource {

    private static final Logger LOG = Logger.getLogger(AlertConfigResource.class);
    private final OrderService orderService;
    private final SecurityService securityService;

    public OrderResource(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response findOrders(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("offset") @DefaultValue("7") int offset,
            @QueryParam("id") int id,
            @QueryParam("status") String status,
            @QueryParam("pickup-location") String pickupLocation,
            @QueryParam("delivery-location") String deliveryLocation,
            @QueryParam("created-at") String createdAt,
            @QueryParam("weight") String weight,
            @QueryParam("size") String size

    ) {
        Map<String, Object> filters = new HashMap<>();
        if (id != 0) filters.put("id", id);

        if (status != null) {
            try {
                OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                filters.put("orderStatus", orderStatus);
            }
            catch (IllegalArgumentException e) {
                LOG.warnf("SECURITY ALERT - Invalid OrderStatus value detected: [%s]", status);

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid status value: " + status)
                        .build();
            }
        }

        if (pickupLocation != null) {
            filters.put("pickupLocation", securityService.sanitize(pickupLocation));
        }
        if (deliveryLocation != null) {
            filters.put("deliveryLocation", securityService.sanitize(deliveryLocation));
        }

        if (createdAt != null) {
            LocalDate date = LocalDate.parse(createdAt);

            OffsetDateTime startOfDay = date.atStartOfDay().atOffset(ZoneOffset.UTC);
            OffsetDateTime endOfDay = startOfDay.plusDays(1);

            filters.put("createdAtFrom", startOfDay);
            filters.put("createdAtTo", endOfDay);
        }

        if (weight != null) {
            try {
                PackageWeight packageWeight = PackageWeight.valueOf(weight.toUpperCase());
                filters.put("packageWeight", packageWeight);
            }
            catch (IllegalArgumentException e) {
                LOG.warnf("SECURITY ALERT - Invalid PackageWeight value received: [%s]", weight);

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid package weight value: " + weight)
                        .build();
            }
        }

        if (size != null) {
            try {
                PackageSize packageSize = PackageSize.valueOf(size.toUpperCase());
                filters.put("packageSize", packageSize);
            }
            catch (IllegalArgumentException e) {
                LOG.warnf("SECURITY ALERT - Invalid PackageSize value received: [%s]", size);

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid package size value: " + size)
                        .build();
            }
        }

        List<OrderResponse> orderResponseList = orderService.findOrders(filters, page, offset);

        return Response.ok(orderResponseList).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"access_token"})
    public Response getDetailedOrderById(@PathParam("id") long id) {
        OrderDetailsResponse orderDetailsResponse = orderService.getDetailedOrderById(id);

        return Response.ok(orderDetailsResponse).build();
    }
}

