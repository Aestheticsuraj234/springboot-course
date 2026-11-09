package com.example.shopease.notification.config;

import java.net.URI;
import java.util.Properties;

/** Typed, validated application settings. Fails fast on missing or unfiltered values. */
public record AppConfig(String environment, String notificationChannel, String senderAddress, URI paymentGatewayUrl) {

    public static AppConfig from(Properties props) {
        return new AppConfig(
                required(props, "app.environment"),
                required(props, "notification.channel"),
                required(props, "notification.from"),
                URI.create(required(props, "payment.gateway.url")));
    }

    private static String required(Properties props, String key) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing setting: " + key);
        }
        if (value.contains("${")) {
            throw new IllegalStateException(
                    "Setting " + key + " was not filtered by Maven (got '" + value + "'); is resource filtering on?");
        }
        return value.trim();
    }
}
