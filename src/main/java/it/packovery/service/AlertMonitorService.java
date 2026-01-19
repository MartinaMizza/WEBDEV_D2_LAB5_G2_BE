package it.packovery.service;

import io.quarkus.scheduler.Scheduled;
import it.packovery.data.model.Alert;
import it.packovery.data.model.AlertConfig;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.AlertStatus;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.repository.AlertConfigRepository;
import it.packovery.data.repository.AlertRepository;
import it.packovery.data.repository.OrderRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class AlertMonitorService {
    private final OrderRepository orderRepository;
    private final AlertRepository alertRepository;
    private final AlertConfigRepository alertConfigRepository;

    public AlertMonitorService(OrderRepository orderRepository, AlertRepository alertRepository, AlertConfigRepository alertConfigRepository) {
        this.orderRepository = orderRepository;
        this.alertRepository = alertRepository;
        this.alertConfigRepository = alertConfigRepository;
    }

    @Scheduled(every = "1m")
    @Transactional
    public void checkDepartureDelays() {
        processAlertCheck("DELAY_DEPARTURE");
    }

    @Scheduled(every = "1m")
    @Transactional
    public void checkDeliveryDelays() {
        processAlertCheck("DELAY_DELIVERY");
    }

    private void processAlertCheck(String configType) {
        AlertConfig alertConfig = alertConfigRepository.find("type", configType).firstResult();
        if (alertConfig == null || !alertConfig.isState()) return;

        int thresholdMinutes = Integer.parseInt(alertConfig.getThreshold());
        OffsetDateTime limitTime = OffsetDateTime.now().minusMinutes(thresholdMinutes);

        List<Order> delayedOrders;
        if (configType.equals("DELAY_DEPARTURE")) {
            delayedOrders = orderRepository.findPendingOrdersOlderThan(limitTime);
        } else {
            delayedOrders = orderRepository.findInTransitOrdersOlderThan(limitTime);
        }

        for (Order order : delayedOrders) {
            if (!alertRepository.existsActiveAlert(order.getId(), alertConfig.getType())) {
                createAlert(order, alertConfig);
            }
        }
    }

    private void createAlert(Order order, AlertConfig alertConfig) {
        Alert alert = new Alert();
        alert.setRelatedOrder(order);
        alert.setStatus(AlertStatus.MEDIUM);
        alert.setIssueResolution(IssueResolution.PENDING);
        alert.setIssueCreationTime(OffsetDateTime.now());
        alert.setTypeAlert(alertConfig.getType());
        alert.setResolutionDescription("Generato automaticamente: " + alertConfig.getDescription());

        alertRepository.persist(alert);
    }
}
