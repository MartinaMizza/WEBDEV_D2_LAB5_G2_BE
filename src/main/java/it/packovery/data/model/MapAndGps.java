package it.packovery.data.model;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "maps_and_gps")
public class MapAndGps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rider_id", nullable = false, foreignKey = @ForeignKey(name = "fk_map_and_gps_rider"))
    private User rider;

    @Column(name = "rider_latitude", nullable = false)
    private Double riderLatitude;

    @Column(name = "rider_longitude", nullable = false)
    private Double riderLongitude;

    @Column(name = "position_timestamp", nullable = false)
    private OffsetDateTime positionTimestamp;

    @Column(name = "pickup_latitude", nullable = false)
    private Double pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false)
    private Double pickupLongitude;

    @Column(name = "delivery_latitude", nullable = false)
    private Double deliveryLatitude;

    @Column(name = "delivery_longitude", nullable = false)
    private Double deliveryLongitude;

    @Column(name = "distance_traveled")
    private Double distanceTraveled;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true) // colonna FK in map_and_gps
    public Order order;

    public MapAndGps() {}

    public MapAndGps(User rider, Double riderLatitude, Double riderLongitude, OffsetDateTime positionTimestamp, Double pickupLatitude, Double pickupLongitude, Double deliveryLatitude, Double deliveryLongitude, Double distanceTraveled, Order order) {
        this.rider = rider;
        this.riderLatitude = riderLatitude;
        this.riderLongitude = riderLongitude;
        this.positionTimestamp = positionTimestamp;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.deliveryLatitude = deliveryLatitude;
        this.deliveryLongitude = deliveryLongitude;
        this.distanceTraveled = distanceTraveled;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getRider() {
        return rider;
    }

    public void setRider(User rider) {
        this.rider = rider;
    }

    public Double getRiderLatitude() {
        return riderLatitude;
    }

    public void setRiderLatitude(Double riderLatitude) {
        this.riderLatitude = riderLatitude;
    }

    public Double getRiderLongitude() {
        return riderLongitude;
    }

    public void setRiderLongitude(Double riderLongitude) {
        this.riderLongitude = riderLongitude;
    }

    public OffsetDateTime getPositionTimestamp() {
        return positionTimestamp;
    }

    public void setPositionTimestamp(OffsetDateTime positionTimestamp) {
        this.positionTimestamp = positionTimestamp;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public Double getDeliveryLatitude() {
        return deliveryLatitude;
    }

    public void setDeliveryLatitude(Double deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }

    public Double getDeliveryLongitude() {
        return deliveryLongitude;
    }

    public void setDeliveryLongitude(Double deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
    }

    public Double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(Double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
