package it.packovery.data.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import it.packovery.data.model.enumModel.ActionType;
import it.packovery.data.model.enumModel.EntityViewed;
import org.bson.types.ObjectId;

import java.time.Instant;

@MongoEntity(collection = "log_records")
public class Logging {

    private ObjectId id;
    private Long userId;
    private ActionType actionType;
    private EntityViewed entityViewed;
    private Instant eventTimestamp;
    private Instant alertCreatedTime;
    private Instant startTime;
    private Instant endTime;
    private Instant messageSentTime;

    public Logging() {}

    public Logging(Long userId, ActionType actionType, EntityViewed entityViewed, Instant eventTimestamp, Instant alertCreatedTime, Instant startTime, Instant endTime, Instant messageSentTime) {
        this.userId = userId;
        this.actionType = actionType;
        this.entityViewed = entityViewed;
        this.eventTimestamp = eventTimestamp;
        this.alertCreatedTime = alertCreatedTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.messageSentTime = messageSentTime;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public EntityViewed getEntityViewed() {
        return entityViewed;
    }

    public void setEntityViewed(EntityViewed entityViewed) {
        this.entityViewed = entityViewed;
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(Instant eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public Instant getAlertCreatedTime() {
        return alertCreatedTime;
    }

    public void setAlertCreatedTime(Instant alertCreatedTime) {
        this.alertCreatedTime = alertCreatedTime;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getMessageSentTime() {
        return messageSentTime;
    }

    public void setMessageSentTime(Instant messageSentTime) {
        this.messageSentTime = messageSentTime;
    }
}
