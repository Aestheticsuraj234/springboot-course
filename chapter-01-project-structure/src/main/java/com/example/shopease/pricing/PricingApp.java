package com.example.shopease.pricing;

import com.example.shopease.pricing.config.PricingConfig;
import com.example.shopease.pricing.domain.Money;
import com.example.shopease.pricing.domain.Order;
import com.example.shopease.pricing.domain.OrderLine;
import com.example.shopease.pricing.policy.DiscountPolicy;
import com.example.shopease.pricing.policy.GstTaxPolicy;
import com.example.shopease.pricing.policy.NoDiscount;
import com.example.shopease.pricing.policy.PercentageDiscount;
import com.example.shopease.pricing.service.OrderPricingService;
import com.example.shopease.pricing.service.PriceBreakdown;
import java.util.List;

/** Composition root: the only place that chooses concrete implementations. */
public final class PricingApp {

    private PricingApp() {
    }

    public static void main(String[] args) {
        PricingConfig config = PricingConfig.fromClasspath("pricing.properties");

        DiscountPolicy discount = config.discountPercent().signum() == 0
                ? new NoDiscount()
                : new PercentageDiscount(config.discountPercent());
        OrderPricingService pricing = new OrderPricingService(discount, new GstTaxPolicy(config.gstPercent()));

        Order order = new Order("ORD-1001", List.of(
                new OrderLine("KEYBOARD-MECH-01", Money.of("3499.00"), 1),
                new OrderLine("MOUSE-WIRELESS-02", Money.of("899.50"), 2)));

        PriceBreakdown price = pricing.price(order);
        System.out.println("Order    : " + order.id());
        System.out.println("Subtotal : " + price.subtotal());
        System.out.println("Discount : " + price.discount() + " (" + config.discountPercent() + "%)");
        System.out.println("GST      : " + price.tax() + " (" + config.gstPercent() + "%)");
        System.out.println("Total    : " + price.total());
    }
}
