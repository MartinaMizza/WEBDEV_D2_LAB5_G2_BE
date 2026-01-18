package it.packovery.web.model;

import java.time.OffsetDateTime;

public class OrderResponse {

    private Long id;
    private String status;
    private String pickupLocation;
    private String deliveryLocation;
    private OffsetDateTime createdAt;
    private String weight;
    private String size;

    public OrderResponse(Long id, String status, String pickupLocation, String deliveryLocation, OffsetDateTime createdAt, String weight, String size) {
        this.id = id;
        this.status = status;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.createdAt = createdAt;
        this.weight = weight;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
