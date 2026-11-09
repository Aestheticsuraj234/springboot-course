package com.example.shopease.pricing.domain;

import java.util.Objects;

/** One product on an order: what was bought, at what unit price, how many. */
public record OrderLine(String sku, Money unitPrice, int quantity) {

    public OrderLine {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
    }

    public Money subtotal() {
        return unitPrice.times(quantity);
    }
}
