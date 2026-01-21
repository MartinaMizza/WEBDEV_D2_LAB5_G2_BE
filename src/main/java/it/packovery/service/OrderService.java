package it.packovery.service;

import it.packovery.data.model.Order;

import it.packovery.data.model.enumModel.OrderStatus;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.LoggingRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.data.repository.OrderRepository;
import it.packovery.service.exception.NotFoundException;
import it.packovery.service.model.Route;
import it.packovery.web.model.OrderDetailsResponse;
import it.packovery.web.model.OrderResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orderRepository;
    private final LoginRepository loginRepository;
    private final LoggingRepository loggingRepository;
    private final RoutingService routingService;
    private final AddressService addressService;

    public OrderService(
            OrderRepository orderRepository,
            LoginRepository loginRepository,
            LoggingRepository loggingRepository,
            RoutingService routingService,
            AddressService addressService
    ) {
        this.orderRepository = orderRepository;
        this.loginRepository = loginRepository;
        this.loggingRepository = loggingRepository;
        this.routingService = routingService;
        this.addressService = addressService;
    }

    public List<OrderResponse> findOrders(
            Map<String, Object> filters,
            String sortingElement,
            String sortingDirection,
            int page,
            int offset) {
        List<Order> ordersList = orderRepository.findOrders(filters, sortingElement, sortingDirection,  page, offset);

        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : ordersList) {
            String[] splitPickupLocation = order.getPickupLocation().split(",");
            String pickupLocation = splitPickupLocation[1].trim() + " " + splitPickupLocation[3].trim();

            String[] splitDeliveryLocation = order.getDeliveryLocation().split(",");
            String deliveryLocation = splitDeliveryLocation[1].trim() + " " + splitDeliveryLocation[3].trim();

            orderResponseList.add(toOrderResponse(order, pickupLocation, deliveryLocation));
        }

        return orderResponseList;
    }

    public OrderDetailsResponse getDetailedOrderById(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
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
                riderDeliveryRoute
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
            Route riderDeliveryRoute
    ){
        return new OrderDetailsResponse(
                order.getId(),
                order.getUser().getName(),
                order.getUser().getSurname(),
                order.getOrderStatus().name(),
                order.getCreatedAt(),
                order.getPackageWeight().name(),
                order.getPackageSize().name(),
                order.getPickupLocation(),
                order.getDeliveryLocation(),
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