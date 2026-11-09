package com.example.shopease.application;

import com.example.shopease.domain.Order;
import com.example.shopease.domain.port.OrderIdGenerator;
import com.example.shopease.domain.port.OrderRepository;
import com.example.shopease.domain.port.PaymentGateway;
import com.example.shopease.domain.port.PaymentResult;
import java.util.Objects;

/**
 * Place-order use case: create the order, persist it as pending, charge the customer,
 * then persist the final state. Depends only on domain ports, never on infrastructure.
 */
public final class PlaceOrderService implements PlaceOrderUseCase {

    private final OrderRepository orders;
    private final PaymentGateway payments;
    private final OrderIdGenerator ids;

    public PlaceOrderService(OrderRepository orders, PaymentGateway payments, OrderIdGenerator ids) {
        this.orders = Objects.requireNonNull(orders, "orders");
        this.payments = Objects.requireNonNull(payments, "payments");
        this.ids = Objects.requireNonNull(ids, "ids");
    }

    @Override
    public Order placeOrder(PlaceOrderCommand command) {
        Order pending = Order.place(ids.nextId(), command.customerId(), command.items());
        orders.save(pending);

        PaymentResult payment = payments.charge(pending.customerId(), pending.total());
        Order completed = payment.approved()
                ? pending.markPaid(payment.reference())
                : pending.markPaymentFailed();
        orders.save(completed);
        return completed;
    }
}
