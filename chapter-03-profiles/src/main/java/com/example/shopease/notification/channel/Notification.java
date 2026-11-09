package com.example.shopease.notification.channel;

/** A message to a customer, independent of how it is delivered. */
public record Notification(String recipient, String subject, String body) {

    public Notification {
        if (recipient == null || recipient.isBlank()) {
            throw new IllegalArgumentException("recipient is required");
        }
        if (subject == null || body == null) {
            throw new IllegalArgumentException("subject and body are required");
        }
    }
}
