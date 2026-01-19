package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.packovery.data.model.Alert;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.web.model.exception.AlertResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;


@ApplicationScoped
public class AlertRepository implements PanacheRepository<Alert> {

    public List<Alert> findPendingAlerts() {
        return list("issueResolution", IssueResolution.PENDING);
    }

}
