package com.example.shopease.pricing.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.example.shopease.pricing.domain.Money;
import com.example.shopease.pricing.domain.Order;
import com.example.shopease.pricing.domain.OrderLine;
import com.example.shopease.pricing.policy.DiscountPolicy;
import com.example.shopease.pricing.policy.TaxPolicy;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderPricingServiceTest {

    // Subtotal: 2 x 1000 + 1 x 500 = 2500
    private final Order order = new Order("ORD-T1", List.of(
            new OrderLine("SKU-A", Money.of("1000"), 2),
            new OrderLine("SKU-B", Money.of("500"), 1)));

    // The policies are interfaces, so tests plug in lambdas - no mocking library needed.
    private final TaxPolicy eighteenPercent = amount -> amount.percentage(BigDecimal.valueOf(18));

    @Test
    void appliesTaxAfterDiscount() {
        DiscountPolicy flat250 = o -> Money.of("250");

        PriceBreakdown price = new OrderPricingService(flat250, eighteenPercent).price(order);

        assertEquals(Money.of("2500"), price.subtotal());
        assertEquals(Money.of("250"), price.discount());
        assertEquals(Money.of("405"), price.tax());
        assertEquals(Money.of("2655"), price.total());
    }

    @Test
    void rejectsDiscountLargerThanSubtotal() {
        DiscountPolicy tooGenerous = o -> Money.of("3000");

        OrderPricingService service = new OrderPricingService(tooGenerous, eighteenPercent);

        assertThrows(IllegalStateException.class, () -> service.price(order));
    }
}
