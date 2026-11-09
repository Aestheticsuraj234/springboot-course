package com.example.shopease.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** A product line within an order. */
public record OrderItem(String productId, int quantity, BigDecimal unitPrice) {

    public OrderItem {
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("productId is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("unit price must be positive: " + unitPrice);
        }
        unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
