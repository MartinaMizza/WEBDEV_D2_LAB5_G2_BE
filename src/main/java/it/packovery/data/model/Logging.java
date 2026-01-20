package it.packovery.data.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import it.packovery.data.model.enumModel.ActionType;
import it.packovery.data.model.enumModel.EntityViewed;
import org.bson.types.ObjectId;
import java.time.OffsetDateTime;

@MongoEntity(collection = "log_records")
public class Logging {

    private ObjectId id;
    private Long userId;
    private ActionType actionType;
    private EntityViewed entityViewed;
    private OffsetDateTime eventTimestamp;
    private OffsetDateTime alertCreatedTime;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private OffsetDateTime messageSentTime;

    public Logging() {}

    public Logging(Long userId, ActionType actionType, EntityViewed entityViewed, OffsetDateTime eventTimestamp, OffsetDateTime alertCreatedTime, OffsetDateTime startTime, OffsetDateTime endTime, OffsetDateTime messageSentTime) {
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
