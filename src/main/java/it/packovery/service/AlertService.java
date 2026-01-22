package it.packovery.service;

import it.packovery.data.model.Alert;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.model.login.Login;
import it.packovery.data.repository.AlertRepository;
import it.packovery.data.repository.LoginRepository;
import it.packovery.service.exception.NotFoundException;
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

    public List<AlertResponse> getAlerts() {
        List<Alert> alerts = alertRepository.findAll().list();
        List<AlertResponse> alertResponses = new ArrayList<>();
        for (Alert alert : alerts) {
            alertResponses.add(toAlertResponse(alert));
        }
        return alertResponses;
    }

    public List<AlertResponse> getPendingAlertsByUser(String email) {
        Login login = loginRepository.findByEmail(email);

        if (login == null) {
            throw new NotFoundException("User not found");
        }
        List<Alert> alertList = alertRepository.findPendingAlertsByUser(login);

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

        return toAlertResponse(existing);
    }

    @Transactional
    public void resolveAlert(Long id, String userEmail) {
        Alert alert = alertRepository.findById(id);

        if (alert == null) {
            throw new NotFoundException("Alert with id " + id + " not found");
        }

        Login login = loginRepository.findByEmail(userEmail);

        if (login == null) {
            throw new NotFoundException("User not found");
        }

        alert.setResolvedBy(login);
        alert.setIssueResolution(IssueResolution.RESOLVED);
        alert.setResolvedTime(OffsetDateTime.now());
        alert.setResolutionDescription("Risolto manualmente");

        List<Alert> unresolvedAlertsByOrder = alertRepository.findPendingAlertsByOrderAndType(alert.getRelatedOrder(), alert.getTypeAlert());

        for (Alert unresolvedAlert : unresolvedAlertsByOrder) {
            alertRepository.delete(unresolvedAlert);
        }
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
