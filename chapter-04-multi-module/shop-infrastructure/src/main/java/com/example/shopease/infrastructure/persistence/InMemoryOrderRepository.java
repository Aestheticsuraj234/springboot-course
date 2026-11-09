package com.example.shopease.infrastructure.persistence;

import com.example.shopease.domain.Order;
import com.example.shopease.domain.port.OrderRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** Thread-safe in-memory storage. Swap for a JPA/JDBC adapter without touching other modules. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> store = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        store.put(order.id(), order);
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(store.get(orderId));
    }
}
