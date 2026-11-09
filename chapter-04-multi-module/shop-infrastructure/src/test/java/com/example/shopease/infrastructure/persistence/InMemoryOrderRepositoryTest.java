package com.example.shopease.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.shopease.domain.Order;
import com.example.shopease.domain.OrderItem;
import com.example.shopease.domain.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

class InMemoryOrderRepositoryTest {

    private final InMemoryOrderRepository repository = new InMemoryOrderRepository();
    private final Order order = Order.place("ORD-1", "CUST-1", List.of(new OrderItem("SKU", 1, BigDecimal.TEN)));

    @Test
    void savingAgainReplacesThePreviousState() {
        repository.save(order);
        repository.save(order.markPaid("PAY-1"));

        assertEquals(OrderStatus.PAID, repository.findById("ORD-1").orElseThrow().status());
    }

    @Test
    void unknownIdIsEmpty() {
        assertTrue(repository.findById("missing").isEmpty());
    }
}
