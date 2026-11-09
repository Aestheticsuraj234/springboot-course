package com.example.shopease.pricing.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PricingConfigTest {

    @Test
    void readsSettingsFromTestResources() {
        PricingConfig config = PricingConfig.fromClasspath("pricing-test.properties");

        assertEquals(new BigDecimal("5"), config.discountPercent());
        assertEquals(new BigDecimal("12"), config.gstPercent());
    }

    @Test
    void failsFastWhenFileIsMissing() {
        assertThrows(IllegalStateException.class, () -> PricingConfig.fromClasspath("nope.properties"));
    }
}
