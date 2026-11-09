package com.example.shopease.application;

import com.example.shopease.domain.Order;

/** Inbound port: what a controller, CLI or message listener calls to place an order. */
@FunctionalInterface
public interface PlaceOrderUseCase {

    /** Places the order and attempts payment; the returned order is PAID or PAYMENT_FAILED. */
    Order placeOrder(PlaceOrderCommand command);
}
