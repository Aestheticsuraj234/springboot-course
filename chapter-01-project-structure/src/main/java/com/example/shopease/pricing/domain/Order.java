package com.example.shopease.pricing.domain;

import java.util.List;

/** A customer order. Knows its own subtotal; knows nothing about discounts or tax. */
public record Order(String id, List<OrderLine> lines) {

    public Order {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("order id is required");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("an order needs at least one line");
        }
        lines = List.copyOf(lines);
    }

    public Money subtotal() {
        return lines.stream().map(OrderLine::subtotal).reduce(Money.ZERO, Money::plus);
    }
}
