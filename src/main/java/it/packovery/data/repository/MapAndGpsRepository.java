package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.packovery.data.model.MapAndGps;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MapAndGpsRepository implements PanacheRepository<MapAndGps> {
}
