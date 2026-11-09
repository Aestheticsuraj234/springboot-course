package com.example.shopease.domain.port;

import com.example.shopease.domain.Order;
import java.util.Optional;

/** Outbound port: how the domain asks for orders to be stored. Implemented in shop-infrastructure. */
public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String orderId);
}
