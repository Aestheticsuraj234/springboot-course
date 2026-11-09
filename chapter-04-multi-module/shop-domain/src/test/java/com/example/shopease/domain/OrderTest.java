package com.example.shopease.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderTest {

    private final Order pending = Order.place("ORD-1", "CUST-1", List.of(
            new OrderItem("SKU-A", 2, new BigDecimal("150.50")),
            new OrderItem("SKU-B", 1, new BigDecimal("99"))));

    @Test
    void newOrderIsPendingWithCorrectTotal() {
        assertEquals(OrderStatus.PENDING_PAYMENT, pending.status());
        assertEquals(new BigDecimal("400.00"), pending.total());
    }

    @Test
    void payingReturnsNewPaidOrderAndLeavesOriginalUntouched() {
        Order paid = pending.markPaid("PAY-1");

        assertEquals(OrderStatus.PAID, paid.status());
        assertEquals("PAY-1", paid.paymentReference().orElseThrow());
        assertEquals(OrderStatus.PENDING_PAYMENT, pending.status());
    }

    @Test
    void cannotPayAnOrderTwice() {
        Order paid = pending.markPaid("PAY-1");

        assertThrows(IllegalStateException.class, () -> paid.markPaid("PAY-2"));
    }

    @Test
    void rejectsOrderWithoutItems() {
        assertThrows(IllegalArgumentException.class, () -> Order.place("ORD-2", "CUST-1", List.of()));
    }
}
