package it.packovery.data.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import it.packovery.data.model.Logging;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LoggingRepository implements PanacheMongoRepository<Logging> {
}
