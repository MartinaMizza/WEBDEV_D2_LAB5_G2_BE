package it.packovery.service;

import it.packovery.data.model.Order;
import it.packovery.data.repository.LoggingRepository;
import it.packovery.data.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class OrderService {

    private final OrderRepository orderRepository;
    private final LoggingService loggingService;

    public OrderService(OrderRepository orderRepository,  LoggingService loggingService) {
        this.orderRepository = orderRepository;
        this.loggingService = loggingService;
    }

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
}