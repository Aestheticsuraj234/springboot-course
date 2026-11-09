package com.example.shopease.invoice.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** One billed item on an invoice. */
public record InvoiceLine(String description, int quantity, BigDecimal unitPrice) {

    public InvoiceLine {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description is required");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }
        Objects.requireNonNull(unitPrice, "unitPrice");
        if (unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unit price cannot be negative: " + unitPrice);
        }
        unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal total() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
