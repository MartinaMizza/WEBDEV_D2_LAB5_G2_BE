//package it.packovery.service;
//
//import it.packovery.data.model.Logging;
//import it.packovery.data.repository.LoggingRepository;
//import jakarta.enterprise.context.ApplicationScoped;
//
//import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//
//@ApplicationScoped
//public class LoggingService {
//
//    private final LoggingRepository loggingRepository;
//
//    public LoggingService(LoggingRepository loggingRepository) {
//        this.loggingRepository = loggingRepository;
//    }
//
//    public void logAccess(Long userId, String azione, String note) {
//        Logging log = new Logging();
//        log.setEntityViewed("ORDER_DETAIL");
//        log.setActionType(azione);
//        log.setEventTimestamp(OffsetDateTime.from(LocalDateTime.now()));
//        log.note = note;
//
//        loggingRepository.persist(log);
//    }
//}
