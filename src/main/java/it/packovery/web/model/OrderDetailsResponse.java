package it.packovery.web.model;

import it.packovery.service.model.Route;

import java.time.OffsetDateTime;

public class OrderDetailsResponse {

    private Long orderId;
    private String userName;
    private String userSurname;
    private String orderStatus;
    private OffsetDateTime createdAt;
    private String weight;
    private String size;
    private String pickupAddress;
    private String deliveryAddress;
    private Route pickupDeliveryRoute;
    private String riderName;
    private String riderSurname;
    private Route pickupRiderRoute;
    private Route riderDeliveryRoute;
    private OffsetDateTime plannedDeliveryTime;
    private String meansOfTransportation;

    public OrderDetailsResponse(Long orderId, String userName, String userSurname, String orderStatus, OffsetDateTime createdAt, String weight, String size, String pickupAddress, String deliveryAddress, Route pickupDeliveryRoute, String riderName, String riderSurname, Route pickupRiderRoute, Route riderDeliveryRoute, OffsetDateTime plannedDeliveryTime, String meansOfTransportation) {
        this.orderId = orderId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.weight = weight;
        this.size = size;
        this.pickupAddress = pickupAddress;
        this.deliveryAddress = deliveryAddress;
        this.pickupDeliveryRoute = pickupDeliveryRoute;
        this.riderName = riderName;
        this.riderSurname = riderSurname;
        this.pickupRiderRoute = pickupRiderRoute;
        this.riderDeliveryRoute = riderDeliveryRoute;
        this.plannedDeliveryTime = plannedDeliveryTime;
        this.meansOfTransportation = meansOfTransportation;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Route getPickupDeliveryRoute() {
        return pickupDeliveryRoute;
    }

    public void setPickupDeliveryRoute(Route pickupDeliveryRoute) {
        this.pickupDeliveryRoute = pickupDeliveryRoute;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderSurname() {
        return riderSurname;
    }

    public void setRiderSurname(String riderSurname) {
        this.riderSurname = riderSurname;
    }

    public Route getPickupRiderRoute() {
        return pickupRiderRoute;
    }

    public void setPickupRiderRoute(Route pickupRiderRoute) {
        this.pickupRiderRoute = pickupRiderRoute;
    }

    public Route getRiderDeliveryRoute() {
        return riderDeliveryRoute;
    }

    public void setRiderDeliveryRoute(Route riderDeliveryRoute) {
        this.riderDeliveryRoute = riderDeliveryRoute;
    }

    public OffsetDateTime getPlannedDeliveryTime() {
        return plannedDeliveryTime;
    }

    public void setPlannedDeliveryTime(OffsetDateTime plannedDeliveryTime) {
        this.plannedDeliveryTime = plannedDeliveryTime;
    }

    public String getMeansOfTransportation() {
        return meansOfTransportation;
    }

    public void setMeansOfTransportation(String meansOfTransportation) {
        this.meansOfTransportation = meansOfTransportation;
    }
}
