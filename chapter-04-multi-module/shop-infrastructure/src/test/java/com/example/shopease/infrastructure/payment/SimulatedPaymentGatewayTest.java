package com.example.shopease.infrastructure.payment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.shopease.domain.port.PaymentResult;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class SimulatedPaymentGatewayTest {

    private final SimulatedPaymentGateway gateway = new SimulatedPaymentGateway(new BigDecimal("1000"));

    @Test
    void approvesUpToTheLimit() {
        PaymentResult result = gateway.charge("CUST-1", new BigDecimal("1000"));

        assertTrue(result.approved());
        assertTrue(result.reference().startsWith("PAY-"));
    }

    @Test
    void declinesAboveTheLimit() {
        PaymentResult result = gateway.charge("CUST-1", new BigDecimal("1000.01"));

        assertFalse(result.approved());
        assertTrue(result.declineReason().contains("limit"));
    }
}
