package it.packovery.service;

import it.packovery.data.model.Alert;
import it.packovery.data.model.MapAndGps;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.model.enumModel.OrderStatus;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.AlertRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.web.model.exception.AlertResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final LoginRepository loginRepository;

    public AlertService(AlertRepository alertRepository, LoginRepository loginRepository) {
        this.alertRepository = alertRepository;
        this.loginRepository = loginRepository;
    }

    public List<AlertResponse> getPendingAlerts() {
        List<Alert> alertList = alertRepository.findPendingAlerts();

        List<AlertResponse> alertResponseList = new ArrayList<>();
        for (Alert alert : alertList) {
            alertResponseList.add(toAlertResponse(alert));
        }

        return alertResponseList;
    }

    public AlertResponse getAlertById(Long id) {
        Alert existing = alertRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Alert with id " + id + " not found");
        }
        AlertResponse alertResponse = toAlertResponse(existing);
        return alertResponse;
    }

    public boolean resolveAlert(Long id,Alert alert) {
        Alert existing = alertRepository.findById(id);
        if (existing == null) {
            return false;
        }
        OffsetDateTime now = OffsetDateTime.now();
        existing.setIssueResolution(IssueResolution.RESOLVED);
        existing.setResolvedBy(alert.getResolvedBy());
        existing.setResolvedTime(now);
        existing.setResolutionDescription(alert.getResolutionDescription());
        return true;
    }

    public boolean automaticResolveAlert() {
        List<Alert> alerts = alertRepository.findPendingAlerts();

        Login systemUser = loginRepository.find("email", "SYSTEM").firstResult();
        if (systemUser == null) {
            Login system = new Login();
            system.setEmail("SYSTEM");
            system.setSystem(true);
            system.setPassword(null);
            loginRepository.persist(system);
            systemUser = system;
        }

        OffsetDateTime now = OffsetDateTime.now();

        for (Alert alert : alerts) {
            Order order = alert.getRelatedOrder();
            if (order == null) continue;

            MapAndGps latestGps = order.getMapAndGps();
            OffsetDateTime positionTimestamp = latestGps != null ? latestGps.getPositionTimestamp() : null;

            switch (alert.getTypeAlert()) {

                case GPS_INTERRUPTED:
                    if (positionTimestamp != null &&
                            positionTimestamp.isAfter(alert.getCreatedTime())) {
                        resolve(alert, systemUser, now,
                                "Risolto automaticamente: nuovo segnale GPS rilevato");
                    }
                    break;

                case DELIVERY_DELAY:
                    if (order.getOrderStatus() == OrderStatus.DELIVERED) {
                        resolve(alert, systemUser, now,
                                "Risolto automaticamente: ordine consegnato");
                    }
                    break;

                case DEPARTURE_DELAY:
                    if (positionTimestamp != null &&
                            positionTimestamp.isAfter(alert.getCreatedTime())) {
                        resolve(alert, systemUser, now,
                                "Risolto automaticamente: ordine partito");
                    }
                    break;
            }
        }

        return true;
    }

    private void resolve(Alert alert, Login systemUser, OffsetDateTime now, String description) {
        alert.setIssueResolution(IssueResolution.RESOLVED);
        alert.setResolvedBy(systemUser);
        alert.setResolvedTime(now);
        alert.setResolutionDescription(description);
    }

    public AlertResponse toAlertResponse(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getRelatedOrder(),
                alert.getTypeAlert(),
                alert.getCreatedTime()
        );
    }
}
