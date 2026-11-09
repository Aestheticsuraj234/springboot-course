package com.example.shopease.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Order aggregate. Immutable: state transitions return a new Order and
 * enforce the rules (only a pending order can be paid or fail payment).
 */
public final class Order {

    private final String id;
    private final String customerId;
    private final List<OrderItem> items;
    private final OrderStatus status;
    private final String paymentReference;

    private Order(String id, String customerId, List<OrderItem> items, OrderStatus status, String paymentReference) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.paymentReference = paymentReference;
    }

    public static Order place(String id, String customerId, List<OrderItem> items) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("order id is required");
        }
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId is required");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("an order needs at least one item");
        }
        return new Order(id, customerId, List.copyOf(items), OrderStatus.PENDING_PAYMENT, null);
    }

    public Order markPaid(String paymentReference) {
        requirePending("mark as paid");
        Objects.requireNonNull(paymentReference, "paymentReference");
        return new Order(id, customerId, items, OrderStatus.PAID, paymentReference);
    }

    public Order markPaymentFailed() {
        requirePending("mark payment as failed");
        return new Order(id, customerId, items, OrderStatus.PAYMENT_FAILED, null);
    }

    private void requirePending(String action) {
        if (status != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Cannot " + action + " order " + id + " in status " + status);
        }
    }

    public BigDecimal total() {
        return items.stream().map(OrderItem::lineTotal).reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    public String id() {
        return id;
    }

    public String customerId() {
        return customerId;
    }

    public List<OrderItem> items() {
        return items;
    }

    public OrderStatus status() {
        return status;
    }

    public Optional<String> paymentReference() {
        return Optional.ofNullable(paymentReference);
    }
}
