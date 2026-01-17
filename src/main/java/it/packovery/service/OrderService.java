package it.packovery.service;

import it.packovery.data.model.Order;

import it.packovery.data.repository.OrderRepository;
import it.packovery.web.model.OrderResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderResponse> findOrders(Map<String, Object> filters, int page, int offset) {
        List<Order> ordersList = orderRepository.findOrders(filters, page, offset);

        List<OrderResponse> orderResponseList = new ArrayList<>();
        for (Order order : ordersList) {
            orderResponseList.add(toOrderResponse(order));
        }

        return orderResponseList;
    }

    public OrderResponse toOrderResponse(Order order){

        return new OrderResponse(
                order.getId(),
                order.getOrderStatus().name(),
                order.getPickupLocation(),
                order.getDeliveryLocation(),
                order.getCreatedAt(),
                order.getPackageWeight().name(),
                order.getPackageSize().name()
        );
    }
    /*
    public Order getOrderDetail(Long orderId, Long userIdOperatore) {
        Order order = orderRepository.findById(orderId);

        if (order == null) {
            throw new NotFoundException("Ordine non trovato");
        }

        String status = (order.getOrderStatus() != null) ? order.getOrderStatus().name() : "UNKNOWN";
        boolean isClosed = "CLOSED".equals(status) || "PAID".equals(status) || "DELIVERED".equals(status);

        String tipoEvento = isClosed ? "ACCESSO_DETTAGLIO_CHIUSO" : "ACCESSO_DETTAGLIO_APERTO";

        loggingService.logAccess(userIdOperatore, tipoEvento, "Visualizzato ordine ID: " + orderId);

        return order;
    }
    */
}