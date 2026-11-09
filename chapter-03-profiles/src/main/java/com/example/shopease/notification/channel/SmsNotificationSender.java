package com.example.shopease.notification.channel;

import java.io.PrintStream;
import java.util.Objects;

/** SMS delivery. Enforces the single-segment limit of 160 characters. */
public final class SmsNotificationSender implements NotificationSender {

    static final int MAX_LENGTH = 160;

    private final String senderId;
    private final PrintStream transport;

    public SmsNotificationSender(String senderId, PrintStream transport) {
        this.senderId = Objects.requireNonNull(senderId, "senderId");
        this.transport = Objects.requireNonNull(transport, "transport");
    }

    @Override
    public String channel() {
        return "sms";
    }

    @Override
    public void send(Notification notification) {
        transport.printf("[sms] %s -> %s: %s%n", senderId, notification.recipient(), text(notification));
    }

    static String text(Notification notification) {
        String text = notification.subject() + ": " + notification.body();
        return text.length() <= MAX_LENGTH ? text : text.substring(0, MAX_LENGTH - 3) + "...";
    }
}
