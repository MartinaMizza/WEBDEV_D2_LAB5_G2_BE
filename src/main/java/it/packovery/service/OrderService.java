package it.packovery.service;

import it.packovery.data.model.Order;

import it.packovery.data.model.login.Login;
import it.packovery.data.repository.LoggingRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.data.repository.OrderRepository;
import it.packovery.service.exception.NotFoundException;
import it.packovery.service.model.Route;
import it.packovery.web.model.OrderDetailsResponse;
import it.packovery.web.model.OrderResponse;
import it.packovery.web.resource.OrderResource;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;
import org.jboss.logging.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderService {

    private static final Logger LOG = Logger.getLogger(OrderResource.class);

    private final OrderRepository orderRepository;
    private final LoginRepository loginRepository;
    private final LoggingRepository loggingRepository;
    private final RoutingService routingService;
    private final AddressService addressService;
    private final CryptoService cryptoService;

    public OrderService(
            OrderRepository orderRepository,
            LoginRepository loginRepository,
            LoggingRepository loggingRepository,
            RoutingService routingService,
            AddressService addressService,
            CryptoService cryptoService
    ) {
        this.orderRepository = orderRepository;
        this.loginRepository = loginRepository;
        this.loggingRepository = loggingRepository;
        this.routingService = routingService;
        this.addressService = addressService;
        this.cryptoService = cryptoService;
    }

    public List<OrderResponse> findOrders(
            Map<String, Object> filters,
            String sortingElement,
            String sortingDirection,
            int page,
            int offset) throws Exception {
        List<Order> ordersList = orderRepository.findOrders(filters, sortingElement, sortingDirection,  page, offset);

        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : ordersList) {
            String decryptedPickupCity = cryptoService.decrypt(order.getPickupCity());
            String decryptedPickupProvince = cryptoService.decrypt(order.getPickupProvince());
            String pickupLocation = decryptedPickupCity + ", " + decryptedPickupProvince;

            String decryptedDeliveryCity = cryptoService.decrypt(order.getDeliveryCity());
            String decryptedDeliveryProvince = cryptoService.decrypt(order.getDeliveryProvince());
            String deliveryLocation = decryptedDeliveryCity + ", " + decryptedDeliveryProvince;

            orderResponseList.add(toOrderResponse(order, pickupLocation, deliveryLocation));
        }

        return orderResponseList;
    }

    public OrderDetailsResponse getDetailedOrderById(Long orderId, String userEmail) throws Exception {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
            MDC.put("event_outcome", "failure");
            LOG.warn("Order with id " + orderId + " not found");
            throw new NotFoundException("Order not found");
        }

        Login login = loginRepository.findByEmail(userEmail);

        if (login == null) {
            throw new NotFoundException("User not found");
        }

//        if (order.getOrderStatus() == OrderStatus.DELIVERED)    {
//            loggingRepository.createViewedClosedOrderLogRecord(login.getId());
//        }
//        else {
//            loggingRepository.createViewedOpenOrderLogRecord(login.getId());
//        }

        String decryptedPickupAddress = cryptoService.decrypt(order.getPickupAddress());
        String decryptedPickupCity = cryptoService.decrypt(order.getPickupCity());
        String decryptedPickupPostalCode = cryptoService.decrypt(order.getPickupPostalCode());
        String decryptedPickupProvince = cryptoService.decrypt(order.getPickupProvince());
        String pickupLocation = decryptedPickupAddress + ", " + decryptedPickupCity + ", " + decryptedPickupPostalCode + ", " + decryptedPickupProvince;

        String decryptedDeliveryAddress = cryptoService.decrypt(order.getDeliveryAddress());
        String decryptedDeliveryCity = cryptoService.decrypt(order.getDeliveryCity());
        String decryptedDeliveryPostalCode = cryptoService.decrypt(order.getDeliveryPostalCode());
        String decryptedDeliveryProvince = cryptoService.decrypt(order.getDeliveryProvince());
        String deliveryLocation = decryptedDeliveryAddress + ", " + decryptedDeliveryCity + ", " + decryptedDeliveryPostalCode + ", " + decryptedDeliveryProvince;



        Route pickupDeliveryRoute = routingService.createRoute(
                order.getMapAndGps().getPickupLongitude(),
                order.getMapAndGps().getPickupLatitude(),
                order.getMapAndGps().getDeliveryLongitude(),
                order.getMapAndGps().getDeliveryLatitude()
        );

        Route pickupRiderRoute = null;
        Route riderDeliveryRoute = null;
        if (order.getMapAndGps().getRiderLongitude() != null && order.getMapAndGps().getRiderLatitude() != null) {
            pickupRiderRoute = routingService.createRoute(
                    order.getMapAndGps().getPickupLongitude(),
                    order.getMapAndGps().getPickupLatitude(),
                    order.getMapAndGps().getRiderLongitude(),
                    order.getMapAndGps().getRiderLatitude()
            );

            riderDeliveryRoute = routingService.createRoute(
                    order.getMapAndGps().getRiderLongitude(),
                    order.getMapAndGps().getRiderLatitude(),
                    order.getMapAndGps().getDeliveryLongitude(),
                    order.getMapAndGps().getDeliveryLatitude()
            );
        }

        /*
        String pickupAddress = addressService.getAddress(
                order.getPickupLocation(),
                order.getMapAndGps().getPickupLatitude(),
                order.getMapAndGps().getPickupLongitude()
        );

        String deliveryAddress = addressService.getAddress(
                order.getDeliveryLocation(),
                order.getMapAndGps().getDeliveryLatitude(),
                order.getMapAndGps().getDeliveryLongitude()
        );
         */

        return toOrderDetailsResponse(
                order,
                pickupDeliveryRoute,
                pickupRiderRoute,
                riderDeliveryRoute,
                pickupLocation,
                deliveryLocation
        );
    }

    public OrderResponse toOrderResponse(Order order, String pickupLocation, String deliveryLocation){

        return new OrderResponse(
                order.getId(),
                order.getOrderStatus().name(),
                pickupLocation,
                deliveryLocation,
                order.getCreatedAt(),
                order.getPackageWeight().name(),
                order.getPackageSize().name()
        );
    }

    public OrderDetailsResponse toOrderDetailsResponse(
            Order order,
            Route pickupDeliveryRoute,
            Route pickupRiderRoute,
            Route riderDeliveryRoute,
            String pickupLocation,
            String deliveryLocation
    ){
        return new OrderDetailsResponse(
                order.getId(),
                order.getUser().getName(),
                order.getUser().getSurname(),
                order.getOrderStatus().name(),
                order.getCreatedAt(),
                order.getPackageWeight().name(),
                order.getPackageSize().name(),
                pickupLocation,
                deliveryLocation,
                pickupDeliveryRoute,
                order.getMapAndGps().getRider().getName(),
                order.getMapAndGps().getRider().getSurname(),
                pickupRiderRoute,
                riderDeliveryRoute,
                order.getPlannedDeliveryTime(),
                order.getMeansOfTransportation().name()

        );
    }
}