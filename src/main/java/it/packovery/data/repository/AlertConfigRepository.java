package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.AlertConfig;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AlertConfigRepository implements PanacheRepository<AlertConfig> {

    public AlertConfig findById(String id) {
        return find("""
                    SELECT ac
                    FROM AlertConfig ac
                    WHERE ac.id = :id
                """,
                Parameters.with("id", id)
        ).firstResult();
    }

    public List<AlertConfig> findActive() {
        return find("""
                    SELECT ac
                    FROM AlertConfig ac
                    WHERE ac.state = true
                """
        ).list();
    }

    public List<AlertConfig> findAllByUserId(Long userId) {
        return find("""
                    SELECT ac
                    FROM AlertConfig ac
                    WHERE ac.login.id = :userId
                """,
                Parameters.with("userId", userId)
        ).list();
    }
}
