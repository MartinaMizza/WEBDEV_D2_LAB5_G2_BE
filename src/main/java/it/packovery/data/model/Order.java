package it.packovery.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "tracking_code", columnDefinition = "VARCHAR", nullable = false, unique = true)
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

    @Column(name = "pickup_address", columnDefinition = "TEXT", nullable = false)
    private String pickupAddress;

    @Column(name = "pickup_city", columnDefinition = "TEXT", nullable = false)
    private String pickupCity;

    @Column(name = "pickup_postal_code", columnDefinition = "TEXT", nullable = false)
    private String pickupPostalCode;

    @Column(name = "pickup_province", columnDefinition = "TEXT", nullable = false)
    private String pickupProvince;

    @Column(name = "delivery_address", columnDefinition = "TEXT", nullable = false)
    private String deliveryAddress;

    @Column(name = "delivery_city", columnDefinition = "TEXT", nullable = false)
    private String deliveryCity;

    @Column(name = "delivery_postal_code", columnDefinition = "TEXT", nullable = false)
    private String deliveryPostalCode;

    @Column(name = "delivery_province", columnDefinition = "TEXT", nullable = false)
    private String deliveryProvince;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "planned_pickup_time", nullable = false)
    private OffsetDateTime plannedPickupTime;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "means_of_transportation",
            columnDefinition = "means_of_transportation_enum"
    )
    private MeansOfTransportation meansOfTransportation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, optional = false)
    @JsonManagedReference
    public MapAndGps mapAndGps;

    public Order() {}

    public Order(String trackingCode, OrderStatus orderStatus, OffsetDateTime plannedDeliveryTime, OffsetDateTime actualDeliveryTime, Long deliveryDelay, PriorityLevel priorityLevel, PackageSize packageSize, PackageWeight packageWeight, boolean oversize, boolean overweight, BigDecimal actualSize, BigDecimal actualWeight, String pickupAddress, String pickupCity, String pickupPostalCode, String pickupProvince, String deliveryAddress, String deliveryCity, String deliveryPostalCode, String deliveryProvince, OffsetDateTime createdAt, OffsetDateTime plannedPickupTime, MeansOfTransportation meansOfTransportation, User user, MapAndGps mapAndGps) {
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
        this.pickupAddress = pickupAddress;
        this.pickupCity = pickupCity;
        this.pickupPostalCode = pickupPostalCode;
        this.pickupProvince = pickupProvince;
        this.deliveryAddress = deliveryAddress;
        this.deliveryCity = deliveryCity;
        this.deliveryPostalCode = deliveryPostalCode;
        this.deliveryProvince = deliveryProvince;
        this.createdAt = createdAt;
        this.plannedPickupTime = plannedPickupTime;
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

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public void setPickupCity(String pickupCity) {
        this.pickupCity = pickupCity;
    }

    public String getPickupPostalCode() {
        return pickupPostalCode;
    }

    public void setPickupPostalCode(String pickupPostalCode) {
        this.pickupPostalCode = pickupPostalCode;
    }

    public String getPickupProvince() {
        return pickupProvince;
    }

    public void setPickupProvince(String pickupProvince) {
        this.pickupProvince = pickupProvince;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public void setDeliveryPostalCode(String deliveryPostalCode) {
        this.deliveryPostalCode = deliveryPostalCode;
    }

    public String getDeliveryProvince() {
        return deliveryProvince;
    }

    public void setDeliveryProvince(String deliveryProvince) {
        this.deliveryProvince = deliveryProvince;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getPlannedPickupTime() {
        return plannedPickupTime;
    }

    public void setPlannedPickupTime(OffsetDateTime plannedPickupTime) {
        this.plannedPickupTime = plannedPickupTime;
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
