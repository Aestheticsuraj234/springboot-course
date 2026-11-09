package com.example.shopease.infrastructure.payment;

import com.example.shopease.domain.port.PaymentGateway;
import com.example.shopease.domain.port.PaymentResult;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Stand-in for a real payment provider. Mimics a common real rule:
 * charges above the per-transaction limit are declined.
 */
public final class SimulatedPaymentGateway implements PaymentGateway {

    private final BigDecimal perTransactionLimit;

    public SimulatedPaymentGateway(BigDecimal perTransactionLimit) {
        this.perTransactionLimit = Objects.requireNonNull(perTransactionLimit, "perTransactionLimit");
    }

    @Override
    public PaymentResult charge(String customerId, BigDecimal amount) {
        if (amount.compareTo(perTransactionLimit) > 0) {
            return PaymentResult.declined("amount " + amount + " exceeds per-transaction limit " + perTransactionLimit);
        }
        return PaymentResult.approved("PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}
