package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.packovery.data.model.Alert;
import it.packovery.data.model.enumModel.IssueResolution;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AlertRepository implements PanacheRepository<Alert> {
    public boolean existsActiveAlert(Long orderId) {
        return count("relatedOrder.id = ?1 and status = ?2", orderId, IssueResolution.PENDING) > 0;
    }

    public List<Alert> findPendingAlerts() {
        return list("issueResolution", IssueResolution.PENDING);
    }
}
