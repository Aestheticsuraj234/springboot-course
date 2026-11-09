package com.example.shopease.pricing.policy;

import com.example.shopease.pricing.domain.Money;
import com.example.shopease.pricing.domain.Order;

/**
 * Decides how much to take off an order.
 * Contract: the returned discount never exceeds the order subtotal.
 */
@FunctionalInterface
public interface DiscountPolicy {

    Money discountFor(Order order);
}
