package com.example.shopease.pricing.policy;

import com.example.shopease.pricing.domain.Money;
import com.example.shopease.pricing.domain.Order;

/** Null-object policy: used when no promotion is running. */
public final class NoDiscount implements DiscountPolicy {

    @Override
    public Money discountFor(Order order) {
        return Money.ZERO;
    }
}
