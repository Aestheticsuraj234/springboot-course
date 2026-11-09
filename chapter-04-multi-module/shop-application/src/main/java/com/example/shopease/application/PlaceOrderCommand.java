package com.example.shopease.application;

import com.example.shopease.domain.OrderItem;
import java.util.List;

/** Input to the place-order use case, e.g. built from an HTTP request body. */
public record PlaceOrderCommand(String customerId, List<OrderItem> items) {

    public PlaceOrderCommand {
        items = items == null ? List.of() : List.copyOf(items);
    }
}
