package it.packovery.service;

import io.quarkus.scheduler.Scheduled;
import it.packovery.data.model.Alert;
import it.packovery.data.model.AlertConfig;
import it.packovery.data.model.MapAndGps;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.AlertStatus;
import it.packovery.data.model.enumModel.AlertType;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.repository.AlertConfigRepository;
import it.packovery.data.repository.AlertRepository;
import it.packovery.data.repository.MapAndGpsRepository;
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
    private final MapAndGpsRepository mapAndGpsRepository;

    public AlertMonitorService(OrderRepository orderRepository, AlertRepository alertRepository, AlertConfigRepository alertConfigRepository, MapAndGpsRepository mapAndGpsRepository) {
        this.orderRepository = orderRepository;
        this.alertRepository = alertRepository;
        this.alertConfigRepository = alertConfigRepository;
        this.mapAndGpsRepository = mapAndGpsRepository;
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

    @Scheduled(every = "1m")
    @Transactional
    public void checkGpsSignal() {
        processAlertCheck("GPS_LOST");
    }


    @Scheduled(every = "30s")
    @Transactional
    public void simulateGpsMovement() {
        List<Order> activeOrders = orderRepository.findInTransitOrders();

        for (Order order : activeOrders) {
            MapAndGps gps = mapAndGpsRepository.findByOrder(order);

            if (gps != null) {
                double latShift = 0.1 + (Math.random() * 0.4);
                double lonShift = 0.1 + (Math.random() * 0.4);

                if (Math.random() > 0.5) latShift *= -1;
                if (Math.random() > 0.5) lonShift *= -1;;

                gps.setRiderLatitude(gps.getRiderLatitude() + latShift);
                gps.setRiderLongitude(gps.getRiderLongitude() + lonShift);

                gps.setPositionTimestamp(OffsetDateTime.now());
            }
        }
    }

    private void processAlertCheck(String configType) {
        AlertConfig alertConfig = alertConfigRepository.find("type", configType).firstResult();
        if (alertConfig == null || !alertConfig.isState()) return;

        int thresholdMinutes = Integer.parseInt(alertConfig.getThreshold());
        OffsetDateTime limitTime = OffsetDateTime.now().minusMinutes(thresholdMinutes);

        List<Order> delayedOrders;
        if (configType.equals("DELAY_DEPARTURE")) {
            delayedOrders = orderRepository.findPendingOrdersOlderThan(limitTime);
        } else if (configType.equals("DELAY_DELIVERY")) {
            delayedOrders = orderRepository.findInTransitOrdersOlderThan(limitTime);
        } else {
            delayedOrders = orderRepository.findOrdersWithLostGps(limitTime);
        }

        for (Order order : delayedOrders) {
            AlertType alertType = AlertType.valueOf(alertConfig.getType());

            if (!alertRepository.existsActiveAlert(order.getId(), alertType)) {
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
        alert.setTypeAlert(AlertType.valueOf(alertConfig.getType()));
        alert.setResolutionDescription("Generato automaticamente: " + alertConfig.getDescription());

        alertRepository.persist(alert);
    }
}
