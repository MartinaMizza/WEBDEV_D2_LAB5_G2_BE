package it.packovery.data.model;

import it.packovery.data.model.enumModel.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_code", nullable = false, unique = true)
    private String trackingCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, columnDefinition = "order_status_enum")
    private OrderStatus orderStatus;

    @Column(name = "planned_delivery_time", nullable = false)
    private OffsetDateTime plannedDeliveryTime;

    @Column(name = "actual_delivery_time")
    private OffsetDateTime actualDeliveryTime;

    @Column(name = "delivery_delay", insertable = false, updatable = false)
    private Long deliveryDelay;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_level", nullable = false, columnDefinition = "priority_level_enum")
    private PriorityLevel priorityLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_size", nullable = false, columnDefinition = "package_size_enum")
    private PackageSize packageSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "package_weight", nullable = false, columnDefinition = "package_weight_enum")
    private PackageWeight packageWeight;

    @Column(name = "oversize", nullable = false)
    private boolean oversize;

    @Column(name = "overweight", nullable = false)
    private boolean overweight;

    @Column(name = "actual_size", precision = 10, scale = 3)
    private BigDecimal actualSize;

    @Column(name = "actual_weight", precision = 10, scale = 3)
    private BigDecimal actualWeight;

    @Column(name = "pickup_location", columnDefinition = "TEXT", nullable = false)
    private String pickupLocation;

    @Column(name = "delivery_location", columnDefinition = "TEXT", nullable = false)
    private String deliveryLocation;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "means_of_transportation")
    private MeansOfTransportation meansOfTransportation = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, optional = false)
    public MapAndGps mapAndGps;

    public Order() {}

    public Order(String trackingCode, OrderStatus orderStatus, OffsetDateTime plannedDeliveryTime, OffsetDateTime actualDeliveryTime, Long deliveryDelay, PriorityLevel priorityLevel, PackageSize packageSize, PackageWeight packageWeight, boolean oversize, boolean overweight, BigDecimal actualSize, BigDecimal actualWeight, String pickupLocation, String deliveryLocation, OffsetDateTime createdAt, MeansOfTransportation meansOfTransportation, User user, MapAndGps mapAndGps) {
        this.trackingCode = trackingCode;
        this.orderStatus = orderStatus;
        this.plannedDeliveryTime = plannedDeliveryTime;
        this.actualDeliveryTime = actualDeliveryTime;
        this.deliveryDelay = deliveryDelay;
        this.priorityLevel = priorityLevel;
        this.packageSize = packageSize;
        this.packageWeight = packageWeight;
        this.oversize = oversize;
        this.overweight = overweight;
        this.actualSize = actualSize;
        this.actualWeight = actualWeight;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.createdAt = createdAt;
        this.meansOfTransportation = meansOfTransportation;
        this.user = user;
        this.mapAndGps = mapAndGps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OffsetDateTime getPlannedDeliveryTime() {
        return plannedDeliveryTime;
    }

    public void setPlannedDeliveryTime(OffsetDateTime plannedDeliveryTime) {
        this.plannedDeliveryTime = plannedDeliveryTime;
    }

    public OffsetDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(OffsetDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public Long getDeliveryDelay() {
        return deliveryDelay;
    }

    public void setDeliveryDelay(Long deliveryDelay) {
        this.deliveryDelay = deliveryDelay;
    }

    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public PackageSize getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(PackageSize packageSize) {
        this.packageSize = packageSize;
    }

    public PackageWeight getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(PackageWeight packageWeight) {
        this.packageWeight = packageWeight;
    }

    public boolean isOversize() {
        return oversize;
    }

    public void setOversize(boolean oversize) {
        this.oversize = oversize;
    }

    public boolean isOverweight() {
        return overweight;
    }

    public void setOverweight(boolean overweight) {
        this.overweight = overweight;
    }

    public BigDecimal getActualSize() {
        return actualSize;
    }

    public void setActualSize(BigDecimal actualSize) {
        this.actualSize = actualSize;
    }

    public BigDecimal getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(BigDecimal actualWeight) {
        this.actualWeight = actualWeight;
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

    public MeansOfTransportation getMeansOfTransportation() {
        return meansOfTransportation;
    }

    public void setMeansOfTransportation(MeansOfTransportation meansOfTransportation) {
        this.meansOfTransportation = meansOfTransportation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MapAndGps getMapAndGps() {
        return mapAndGps;
    }

    public void setMapAndGps(MapAndGps mapAndGps) {
        this.mapAndGps = mapAndGps;
    }
}
