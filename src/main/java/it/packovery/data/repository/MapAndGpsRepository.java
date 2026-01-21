package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.packovery.data.model.MapAndGps;
import it.packovery.data.model.Order;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MapAndGpsRepository implements PanacheRepository<MapAndGps> {
    public MapAndGps findByOrder(Order order) {
        return find("order", order).firstResult();
    }
}
