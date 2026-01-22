package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import it.packovery.data.model.Order;
import it.packovery.data.model.enumModel.OrderStatus;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    public List<Order> findOrders(
            Map<String, Object> filters,
            String sortingElement,
            String sortingDirection,
            int page,
            int offset
    ) {
        Sort sortBy = sortingDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortingElement).ascending()
                : Sort.by(sortingElement).descending();

        if (filters != null && !filters.isEmpty()) {
            List<String> conditions = new ArrayList<>();
            Parameters params = new Parameters();

            filters.forEach((key, value) -> {
                if (value == null) return;

                switch (key) {

                    case "createdAtFrom" -> {
                        conditions.add("createdAt >= :createdAtFrom");
                        params.and("createdAtFrom", value);
                    }

                    case "createdAtTo" -> {
                        conditions.add("createdAt < :createdAtTo");
                        params.and("createdAtTo", value);
                    }

                    case "pickupLocation" -> {
                        String pickupLocation = value.toString().trim();
                        conditions.add("pickupLocation ILIKE :pickupLocation");
                        params.and("pickupLocation", "%" + pickupLocation + "%");
                    }

                    case "deliveryLocation" -> {
                        String deliveryLocation = value.toString().trim();
                        conditions.add("deliveryLocation ILIKE :deliveryLocation");
                        params.and("deliveryLocation", "%" + deliveryLocation + "%");
                    }

                    default -> {
                        conditions.add(key + " = :" + key);
                        params.and(key, value);
                    }
                }
            });

            String finalQuery = String.join(" AND ", conditions);

            return find(finalQuery, sortBy, params)
                    .page(Page.of(page, offset))
                    .list();
        }
        else {
            return findAll(sortBy).page(Page.of(page, offset)).list();
        }

    }

    public List<Order> findByStatus(OrderStatus status) {
        return find("""
                    SELECT o
                    FROM Order o
                    WHERE o.orderStatus = :status
                """,
                Parameters.with("status", status)
        ).list();
    }

    public List<Order> findPendingOrdersOlderThan(OffsetDateTime limitTime) {
        return find("CAST(orderStatus as String) = 'PENDING' AND plannedDeliveryTime < ?1", limitTime).list();
    }

    public List<Order> findInTransitOrdersOlderThan(OffsetDateTime limitTime) {
        return find("CAST(orderStatus as String) IN ('SHIPPED', 'IN_TRANSIT') AND plannedDeliveryTime < ?1", limitTime).list();
    }

    public List<Order> findOrdersWithLostGps(OffsetDateTime timeout) {
        return find("CAST(orderStatus as String) IN ('SHIPPED', 'IN_TRANSIT') AND " +
                        "id IN (SELECT mg.order.id FROM MapAndGps mg WHERE mg.positionTimestamp < ?1)",
                timeout).list();
    }

    public List<Order> findInTransitOrders() {
        return find("CAST(orderStatus as String) IN ('SHIPPED', 'IN_TRANSIT')").list();
    }
}
