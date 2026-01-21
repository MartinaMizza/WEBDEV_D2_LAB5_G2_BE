package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import it.packovery.data.model.Message;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageRepository implements PanacheRepository<Message> {
}
