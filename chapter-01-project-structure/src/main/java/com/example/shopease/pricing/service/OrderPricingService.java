package com.example.shopease.pricing.service;

import com.example.shopease.pricing.domain.Money;
import com.example.shopease.pricing.domain.Order;
import com.example.shopease.pricing.policy.DiscountPolicy;
import com.example.shopease.pricing.policy.TaxPolicy;
import java.util.Objects;

/**
 * Orchestrates pricing: subtotal, then discount, then tax on the discounted amount.
 * Depends only on the policy abstractions, so new promotions or tax rules need no change here.
 */
public final class OrderPricingService {

    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;

    public OrderPricingService(DiscountPolicy discountPolicy, TaxPolicy taxPolicy) {
        this.discountPolicy = Objects.requireNonNull(discountPolicy, "discountPolicy");
        this.taxPolicy = Objects.requireNonNull(taxPolicy, "taxPolicy");
    }

    public PriceBreakdown price(Order order) {
        Money subtotal = order.subtotal();
        Money discount = discountPolicy.discountFor(order);
        if (discount.isGreaterThan(subtotal)) {
            throw new IllegalStateException("Discount " + discount + " exceeds subtotal " + subtotal);
        }
        Money taxable = subtotal.minus(discount);
        Money tax = taxPolicy.taxOn(taxable);
        return new PriceBreakdown(subtotal, discount, tax, taxable.plus(tax));
    }
}
