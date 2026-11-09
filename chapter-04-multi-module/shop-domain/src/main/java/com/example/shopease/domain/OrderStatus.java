package com.example.shopease.domain;

/** Lifecycle of an order: PENDING_PAYMENT moves to exactly one of the final states. */
public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    PAYMENT_FAILED
}
