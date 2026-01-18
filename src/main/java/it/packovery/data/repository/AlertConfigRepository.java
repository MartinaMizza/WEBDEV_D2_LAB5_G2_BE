package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import it.packovery.data.model.AlertConfig;
import jakarta.enterprise.context.ApplicationScoped;

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
}
