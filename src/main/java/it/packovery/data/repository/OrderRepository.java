package it.packovery.data.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import it.packovery.data.model.Order;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class OrderRepository implements PanacheRepository<Order> {

    public List<Order> findOrders(Map<String, Object> filters, int page, int offset) {
        Sort sortBy = Sort.by("createdAt").descending();

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
}
