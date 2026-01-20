package it.packovery.service;

import it.packovery.data.model.Logging;
import it.packovery.data.model.enumModel.ActionType;
import it.packovery.data.model.enumModel.EntityViewed;
import it.packovery.data.repository.LoggingRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@ApplicationScoped
public class LoggingService {

    private final LoggingRepository loggingRepository;

    public LoggingService(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    public void logAccess(Long userId, String azione, String note) {
        Logging log = new Logging();
        log.setEntityViewed(EntityViewed.valueOf("ORDER_DETAIL"));
        log.setActionType(ActionType.valueOf(azione));
        log.setEventTimestamp(OffsetDateTime.from(LocalDateTime.now()));

        loggingRepository.persist(log);
    }
}
