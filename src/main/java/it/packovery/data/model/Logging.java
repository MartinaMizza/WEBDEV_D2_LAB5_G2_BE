package it.packovery.data.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import org.bson.types.ObjectId;
import java.time.OffsetDateTime;


@MongoEntity(collection = "loggings")
public class Logging {
    @Id
    ObjectId id;
    ObjectId userId;
    ObjectId driverId;
    String actionType;
    String entityViewed;
    OffsetDateTime eventTimestamp;
    OffsetDateTime alertCreatedTime;
    OffsetDateTime startTime;
    OffsetDateTime endTime;
    OffsetDateTime messageSentTime;

    public Logging(ObjectId id, ObjectId userId, ObjectId driverId, String actionType, String entityViewed, OffsetDateTime eventTimestamp, OffsetDateTime alertCreatedTime, OffsetDateTime startTime, OffsetDateTime endTime, OffsetDateTime messageSentTime) {
        this.id = id;
        this.userId = userId;
        this.driverId = driverId;
        this.actionType = actionType;
        this.entityViewed = entityViewed;
        this.eventTimestamp = eventTimestamp;
        this.alertCreatedTime = alertCreatedTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.messageSentTime = messageSentTime;
    }

    public Logging() {}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getDriverId() {
        return driverId;
    }

    public void setDriverId(ObjectId driverId) {
        this.driverId = driverId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getEntityViewed() {
        return entityViewed;
    }

    public void setEntityViewed(String entityViewed) {
        this.entityViewed = entityViewed;
    }

    public OffsetDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(OffsetDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public OffsetDateTime getAlertCreatedTime() {
        return alertCreatedTime;
    }

    public void setAlertCreatedTime(OffsetDateTime alertCreatedTime) {
        this.alertCreatedTime = alertCreatedTime;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public OffsetDateTime getMessageSentTime() {
        return messageSentTime;
    }

    public void setMessageSentTime(OffsetDateTime messageSentTime) {
        this.messageSentTime = messageSentTime;
    }
}
