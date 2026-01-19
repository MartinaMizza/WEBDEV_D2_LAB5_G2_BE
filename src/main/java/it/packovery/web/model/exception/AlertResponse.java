package it.packovery.web.model.exception;

import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.IssueResolution;

import java.time.OffsetDateTime;

public class AlertResponse {

    private Long id;
    private Order idOrder;
    private String typeAlert;
    private OffsetDateTime createdTime;

    public AlertResponse(Long id, Order idOrder, String typeAlert, OffsetDateTime createdTime) {
        this.id = id;
        this.idOrder = idOrder;
        this.typeAlert = typeAlert;
        this.createdTime = createdTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Order idOrder) {
        this.idOrder = idOrder;
    }

    public String getTypeAlert() {
        return typeAlert;
    }

    public void setTypeAlert(String typeAlert) {
        this.typeAlert = typeAlert;
    }

    public OffsetDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(OffsetDateTime createdTime) {
        this.createdTime = createdTime;
    }

}
