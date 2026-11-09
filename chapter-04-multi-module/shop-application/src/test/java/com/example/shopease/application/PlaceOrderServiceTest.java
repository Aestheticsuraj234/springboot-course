package com.example.shopease.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.shopease.domain.Order;
import com.example.shopease.domain.OrderItem;
import com.example.shopease.domain.OrderStatus;
import com.example.shopease.domain.port.OrderRepository;
import com.example.shopease.domain.port.PaymentResult;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * Note: this module does NOT depend on shop-infrastructure, so the test uses its own fakes.
 * That's the point - use cases are testable without any real adapter.
 */
class PlaceOrderServiceTest {

    private static final class RecordingRepository implements OrderRepository {
        final List<Order> saved = new ArrayList<>();

        @Override
        public void save(Order order) {
            saved.add(order);
        }

        @Override
        public Optional<Order> findById(String orderId) {
            return saved.stream().filter(o -> o.id().equals(orderId)).reduce((first, last) -> last);
        }
    }

    private final RecordingRepository repository = new RecordingRepository();
    private final PlaceOrderCommand command = new PlaceOrderCommand("CUST-1",
            List.of(new OrderItem("SKU-A", 2, new BigDecimal("500"))));

    @Test
    void approvedPaymentMarksOrderPaid() {
        PlaceOrderService service = new PlaceOrderService(
                repository, (customer, amount) -> PaymentResult.approved("PAY-42"), () -> "ORD-1");

        Order order = service.placeOrder(command);

        assertEquals(OrderStatus.PAID, order.status());
        assertEquals("PAY-42", order.paymentReference().orElseThrow());
        assertEquals(List.of(OrderStatus.PENDING_PAYMENT, OrderStatus.PAID),
                repository.saved.stream().map(Order::status).toList());
    }

    @Test
    void declinedPaymentMarksOrderFailed() {
        PlaceOrderService service = new PlaceOrderService(
                repository, (customer, amount) -> PaymentResult.declined("insufficient funds"), () -> "ORD-2");

        Order order = service.placeOrder(command);

        assertEquals(OrderStatus.PAYMENT_FAILED, order.status());
        assertEquals(OrderStatus.PAYMENT_FAILED, repository.findById("ORD-2").orElseThrow().status());
    }

    @Test
    void chargesTheOrderTotal() {
        List<BigDecimal> charged = new ArrayList<>();
        PlaceOrderService service = new PlaceOrderService(repository, (customer, amount) -> {
            charged.add(amount);
            return PaymentResult.approved("PAY-1");
        }, () -> "ORD-3");

        service.placeOrder(command);

        assertEquals(List.of(new BigDecimal("1000.00")), charged);
    }
}
