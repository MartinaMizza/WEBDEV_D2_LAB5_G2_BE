package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.Alert;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.AlertType;
import it.packovery.data.model.enumModel.IssueResolution;
import it.packovery.data.model.login.Login;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AlertRepository implements PanacheRepository<Alert> {

    public boolean existsActiveAlert(Long orderId, AlertType type) {
        return count("relatedOrder.id = ?1 AND typeAlert = ?2 AND issueResolution = ?3",
                orderId, type, IssueResolution.PENDING) > 0;
    }

    public List<Alert> findPendingAlertsByUser(Login login) {
        return find("""
                    SELECT a
                    FROM Alert a
                    WHERE a.issueResolution = 'PENDING' AND a.alterConfig.login.id = :id
                """,
                Parameters.with("id", login.getId())
        ).list();
    }

    public List<Alert> findPendingAlerts() {
        return find("""
                    SELECT a
                    FROM Alert a
                    WHERE a.issueResolution = 'PENDING'
                """
        ).list();
    }

    public List<Alert> findUnresolvedByOrder(Order order) {
        return find("""
                    SELECT a
                    FROM Alert a
                    WHERE a.relatedOrder.id = :orderId AND a.issueResolution = 'PENDING'
                """
                , Parameters.with("orderId", order.getId())
        ).list();
    }

    public List<Alert> findPendingAlertsByOrderAndType(Order order, AlertType type) {
        return find("""
                    SELECT a
                    FROM Alert a
                    WHERE a.relatedOrder.id = :orderId AND a.issueResolution = 'PENDING' AND a.typeAlert = :type
                """,
                Parameters.with("orderId", order.getId())
                        .and("type", type)
        ).list();
    }
}
