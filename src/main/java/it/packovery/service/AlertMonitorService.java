package it.packovery.service;

import io.quarkus.scheduler.Scheduled;
import it.packovery.data.model.Alert;
import it.packovery.data.model.AlertConfig;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.AlertStatus;
import it.packovery.data.model.enumModel.AlertType;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.model.enumModel.OrderStatus;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class AlertMonitorService {
    private final OrderRepository orderRepository;
    private final AlertRepository alertRepository;
    private final AlertConfigRepository alertConfigRepository;
    private final LoginRepository loginRepository;
    private final MapAndGpsRepository mapAndGpsRepository;

    public AlertMonitorService(OrderRepository orderRepository, AlertRepository alertRepository, AlertConfigRepository alertConfigRepository, LoginRepository loginRepository, MapAndGpsRepository mapAndGpsRepository) {
        this.orderRepository = orderRepository;
        this.alertRepository = alertRepository;
        this.alertConfigRepository = alertConfigRepository;
        this.loginRepository = loginRepository;
        this.mapAndGpsRepository = mapAndGpsRepository;
    }

    @Scheduled(every = "1m")
    public void checkAllAlerts() {
        OffsetDateTime now = OffsetDateTime.now();

        List<AlertConfig> activeAlertConfigs = alertConfigRepository.findActive();

        Map<String, List<AlertConfig>> alertConfigsByType = activeAlertConfigs.stream()
                .collect(Collectors.groupingBy(AlertConfig::getType));

        List<Order> readyOrders = orderRepository.findByStatus(OrderStatus.READY);
        List<Order> inTransitOrders = orderRepository.findByStatus(OrderStatus.IN_TRANSIT);
        System.out.println(inTransitOrders.size());
        System.out.println(readyOrders.size());

        for (AlertConfig alert : alertConfigsByType.getOrDefault("Segnale GPS interrotto", List.of())) {
            int thresholdMinutes = parseThresholdToMinutes(alert.getThreshold());

            for (Order order : inTransitOrders) {
                handleExistingAlerts(order, AlertType.GPS_INTERRUPTED, now);
                OffsetDateTime limit = order.getMapAndGps().getPositionTimestamp().plusMinutes(thresholdMinutes);
                if (now.isAfter(limit)) createAlert(order, alert, AlertType.GPS_INTERRUPTED, AlertStatus.HIGH);
            }
        }

        for (AlertConfig alert : alertConfigsByType.getOrDefault("Ritardo partenza ordine", List.of())) {
            int thresholdMinutes = parseThresholdToMinutes(alert.getThreshold());
            for (Order order : readyOrders) {
                handleExistingAlerts(order, AlertType.DEPARTURE_DELAY, now);
                OffsetDateTime limit = order.getPlannedPickupTime().plusMinutes(thresholdMinutes);
                if (now.isAfter(limit)) createAlert(order, alert, AlertType.DEPARTURE_DELAY, AlertStatus.MEDIUM);
            }
        }

        for (AlertConfig alert : alertConfigsByType.getOrDefault("Ritardo consegna ordine", List.of())) {
            int thresholdMinutes = parseThresholdToMinutes(alert.getThreshold());
            for (Order order : inTransitOrders) {
                handleExistingAlerts(order, AlertType.DELIVERY_DELAY, now);
                OffsetDateTime limit = order.getPlannedDeliveryTime().plusMinutes(thresholdMinutes);
                if (now.isAfter(limit)) createAlert(order, alert, AlertType.DELIVERY_DELAY, AlertStatus.MEDIUM);
            }
        }
    }

    @Transactional
    public void handleExistingAlerts(Order order, AlertType alertType, OffsetDateTime now) {
        List<Alert> activeAlert = alertRepository.findPendingAlertsByOrderAndType(order, alertType);

        if (activeAlert.isEmpty()) return;

        for (Alert alert : activeAlert) {
            if (alert.getTypeAlert().equals(AlertType.GPS_INTERRUPTED)) {
                int thresholdMinutes = parseThresholdToMinutes(alert.getAlterConfig().getThreshold());
                OffsetDateTime limit = order.getMapAndGps().getPositionTimestamp().plusMinutes(thresholdMinutes);

                if (order.getMapAndGps().getPositionTimestamp() != null && now.isBefore(limit)) {
                    selfResolveAlert(alert);
                }
            }
            else if (alert.getTypeAlert().equals(AlertType.DEPARTURE_DELAY)) {
                int thresholdMinutes = parseThresholdToMinutes(alert.getAlterConfig().getThreshold());
                OffsetDateTime limit = order.getPlannedPickupTime().plusMinutes(thresholdMinutes);

                if (order.getPlannedPickupTime() != null && now.isBefore(limit)) {
                    selfResolveAlert(alert);
                }
            }
            else if (alert.getTypeAlert().equals(AlertType.DELIVERY_DELAY)) {
                int thresholdMinutes = parseThresholdToMinutes(alert.getAlterConfig().getThreshold());
                OffsetDateTime limit = order.getPlannedDeliveryTime().plusMinutes(thresholdMinutes);

                if (order.getPlannedDeliveryTime() != null && now.isBefore(limit)) {
                    selfResolveAlert(alert);
                }
            }
            else {
                throw new IllegalStateException("Unexpected alert type");
            }
        }
    }

    @Transactional
    public void createAlert(Order order, AlertConfig config, AlertType type, AlertStatus status) {
        boolean exists = alertRepository.existsActiveAlert(order.getId(), type);

        if (exists) return;

        Alert alert = new Alert(
                status,
                IssueResolution.PENDING,
                OffsetDateTime.now(),
                order,
                OffsetDateTime.now(),
                null,
                null,
                null,
                config,
                type
        );

        alertRepository.persist(alert);
    }

    @Transactional
    public void selfResolveAlert(Alert alert) {
        Login loginSystem = loginRepository.findByEmail("system@packovery.it");
        alert.setResolvedBy(loginSystem);
        alert.setIssueResolution(IssueResolution.RESOLVED);
        alert.setResolvedTime(OffsetDateTime.now());
        alert.setResolutionDescription("Risolto automaticamente");
    }

    public int parseThresholdToMinutes(String threshold) {
        String[] parts = threshold.split(":");
        if (parts.length != 2) throw new IllegalArgumentException("Threshold must be HH:mm");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }
}
