package com.example.shopease.pricing.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.util.Properties;

/** Pricing settings read from a .properties file on the classpath (src/main/resources). */
public record PricingConfig(BigDecimal discountPercent, BigDecimal gstPercent) {

    public static PricingConfig fromClasspath(String resource) {
        try (InputStream in = PricingConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing classpath resource: " + resource);
            }
            Properties props = new Properties();
            props.load(in);
            return new PricingConfig(
                    required(props, "pricing.discount.percent"),
                    required(props, "pricing.gst.percent"));
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read " + resource, e);
        }
    }

    private static BigDecimal required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing pricing setting: " + key);
        }
        return new BigDecimal(value.trim());
    }
}
