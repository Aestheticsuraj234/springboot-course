package com.example.shopease.pricing.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void normalisesToTwoDecimalPlaces() {
        assertEquals(Money.of("10.00"), Money.of("10"));
    }

    @Test
    void roundsPercentageHalfUp() {
        assertEquals(Money.of("858.28"), Money.of("4768.20").percentage(BigDecimal.valueOf(18)));
    }

    @Test
    void rejectsNegativeAmounts() {
        assertThrows(IllegalArgumentException.class, () -> Money.of("-1"));
    }
}
