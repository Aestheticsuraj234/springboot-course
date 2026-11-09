package com.example.shopease.app;

import com.example.shopease.application.PlaceOrderCommand;
import com.example.shopease.application.PlaceOrderService;
import com.example.shopease.application.PlaceOrderUseCase;
import com.example.shopease.domain.Order;
import com.example.shopease.domain.OrderItem;
import com.example.shopease.infrastructure.id.SequentialOrderIdGenerator;
import com.example.shopease.infrastructure.payment.SimulatedPaymentGateway;
import com.example.shopease.infrastructure.persistence.InMemoryOrderRepository;
import java.math.BigDecimal;
import java.util.List;

/**
 * Composition root. The only module that sees both the use cases and the adapters;
 * in a Spring Boot app this wiring would be done by the IoC container.
 */
public final class ShopApplication {

    private ShopApplication() {
    }

    public static void main(String[] args) {
        PlaceOrderUseCase placeOrder = new PlaceOrderService(
                new InMemoryOrderRepository(),
                new SimulatedPaymentGateway(new BigDecimal("100000")),
                new SequentialOrderIdGenerator());

        print(placeOrder.placeOrder(new PlaceOrderCommand("CUST-ASHA", List.of(
                new OrderItem("BOOK-CLEAN-CODE", 1, new BigDecimal("699")),
                new OrderItem("PEN-GEL-BLUE", 10, new BigDecimal("25"))))));

        print(placeOrder.placeOrder(new PlaceOrderCommand("CUST-RAVI", List.of(
                new OrderItem("LAPTOP-PRO-16", 1, new BigDecimal("189990"))))));
    }

    private static void print(Order order) {
        System.out.printf("%s | customer=%s | total=INR %s | status=%s%s%n",
                order.id(), order.customerId(), order.total().toPlainString(), order.status(),
                order.paymentReference().map(ref -> " | ref=" + ref).orElse(""));
    }
}
