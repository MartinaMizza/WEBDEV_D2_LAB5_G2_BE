package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.packovery.data.model.Alert;
import it.packovery.data.model.enumModel.AlertType;
import it.packovery.data.model.enumModel.IssueResolution;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AlertRepository implements PanacheRepository<Alert> {
    public boolean existsActiveAlert(Long orderId, AlertType type) {
        return count("relatedOrder.id = ?1 AND typeAlert = ?2 AND issueResolution = ?3",
                orderId, type, IssueResolution.PENDING) > 0;
    }

    public List<Alert> findPendingAlerts() {
        return list("issueResolution", IssueResolution.PENDING);
    }
}
