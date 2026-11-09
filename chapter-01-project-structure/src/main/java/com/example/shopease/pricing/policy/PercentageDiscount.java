package com.example.shopease.pricing.policy;

import com.example.shopease.pricing.domain.Money;
import com.example.shopease.pricing.domain.Order;
import java.math.BigDecimal;
import java.util.Objects;

/** A sale-wide discount, e.g. "10% off everything". */
public final class PercentageDiscount implements DiscountPolicy {

    private final BigDecimal percent;

    public PercentageDiscount(BigDecimal percent) {
        Objects.requireNonNull(percent, "percent");
        if (percent.signum() < 0 || percent.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("discount percent must be between 0 and 100: " + percent);
        }
        this.percent = percent;
    }

    @Override
    public Money discountFor(Order order) {
        return order.subtotal().percentage(percent);
    }
}
