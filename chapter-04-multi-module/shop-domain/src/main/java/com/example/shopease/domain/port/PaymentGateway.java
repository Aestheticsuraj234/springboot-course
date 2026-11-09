package com.example.shopease.domain.port;

import java.math.BigDecimal;

/** Outbound port: charge a customer. Razorpay, Stripe or a fake - the domain doesn't know. */
@FunctionalInterface
public interface PaymentGateway {

    PaymentResult charge(String customerId, BigDecimal amount);
}
