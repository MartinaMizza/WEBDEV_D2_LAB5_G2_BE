package it.packovery.data.repository;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import it.packovery.data.model.Logging;
import it.packovery.data.model.enumModel.ActionType;
import it.packovery.data.model.enumModel.EntityViewed;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@ApplicationScoped
public class LoggingRepository implements PanacheMongoRepository<Logging> {

    public Logging findLatestLoginByUserId(Long userId) {
        return find(
                "userId = ?1 and actionType = ?2",
                userId,
                ActionType.APPLICATION_LOGIN,
                Sort.descending("eventTimestamp")
        )
                .firstResult();
    }

    public void createLoginLogRecord(Long userId) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        Instant now = offsetDateTime.toInstant();
        Instant expectedEndSession = now.plusSeconds(3600);

        Logging log = new Logging(
                userId,
                ActionType.APPLICATION_LOGIN,
                null,
                now,
                null,
                now,
                expectedEndSession,
                null
        );

        persist(log);
    }

    public void createUserBlockedLogRecord(Long userId) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        Instant now = offsetDateTime.toInstant();

        Logging log = new Logging(
                userId,
                ActionType.USER_BLOCKED,
                null,
                now,
                null,
                null,
                null,
                null
        );

        persist(log);
    }

    public void createViewedOpenOrderLogRecord(Long userId) {
        OffsetDateTime offsetDateTimeNow = OffsetDateTime.now();
        Instant instantNow = offsetDateTimeNow.toInstant();

        Logging latestLoginLogRecord = findLatestLoginByUserId(userId);
        Instant latestLoginTime = latestLoginLogRecord.getEventTimestamp();
        Instant expectedEndSession = latestLoginTime.plusSeconds(3600);

        Logging log = new Logging(
                userId,
                ActionType.VIEWED_OPEN_ORDER_DETAILS,
                EntityViewed.OPEN_ORDER,
                instantNow,
                null,
                latestLoginLogRecord.getEventTimestamp(),
                expectedEndSession,
                null
        );

        persist(log);
    }

    public void createViewedClosedOrderLogRecord(Long userId) {
        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        Instant now = offsetDateTime.toInstant();

        Logging latestLoginLogRecord = findLatestLoginByUserId(userId);
        Instant latestLoginTime = latestLoginLogRecord.getEventTimestamp();
        Instant expectedEndSession = latestLoginTime.plusSeconds(3600);

        Logging log = new Logging(
                userId,
                ActionType.VIEWED_CLOSED_ORDER_DETAILS,
                EntityViewed.CLOSED_ORDER,
                now,
                null,
                latestLoginTime,
                expectedEndSession,
                null
        );

        persist(log);
    }

    public void updateLogRecordsAtLogout(Long userId) {
        Instant now = Instant.now();

        Logging latestLoginLogRecord = findLatestLoginByUserId(userId);

        List<Logging> loggingList = find("{'userId': ?1, 'endTime': ?2}", userId, latestLoginLogRecord.getEndTime()).list();

        for (Logging log : loggingList) {
            log.setEndTime(now);
            update(log);
        }
    }
}
