package com.example.shopease.notification.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class AppConfigTest {

    @Test
    void mapsAllSettings() {
        AppConfig config = AppConfig.from(props("staging", "email", "a@b.example", "https://pay.example/v1"));

        assertEquals("staging", config.environment());
        assertEquals("email", config.notificationChannel());
        assertEquals(URI.create("https://pay.example/v1"), config.paymentGatewayUrl());
    }

    @Test
    void rejectsUnfilteredPlaceholders() {
        Properties props = props("${app.environment}", "email", "a@b.example", "https://pay.example/v1");

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> AppConfig.from(props));
        assertTrue(e.getMessage().contains("not filtered"), e.getMessage());
    }

    @Test
    void rejectsMissingSettings() {
        assertThrows(IllegalStateException.class, () -> AppConfig.from(new Properties()));
    }

    private static Properties props(String env, String channel, String from, String url) {
        Properties props = new Properties();
        props.setProperty("app.environment", env);
        props.setProperty("notification.channel", channel);
        props.setProperty("notification.from", from);
        props.setProperty("payment.gateway.url", url);
        return props;
    }
}
